package org.shieldx.oracle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.config.RiskScoreProperties;
import org.shieldx.oracle.entity.JailEvent;
import org.shieldx.oracle.entity.RiskScore;
import org.shieldx.oracle.entity.RiskTier;
import org.shieldx.oracle.entity.Validator;
import org.shieldx.oracle.exception.ValidatorNotFoundException;
import org.shieldx.oracle.repository.JailEventRepository;
import org.shieldx.oracle.repository.RiskScoreRepository;
import org.shieldx.oracle.repository.ValidatorRepository;
import org.shieldx.oracle.service.RiskService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveRiskServiceImpl implements RiskService {
    public static final String CURRENT_RISK_MODEL_VERSION = "v1.0.0";

    private final RiskScoreProperties props;
    private final JailEventRepository jailRepo;
    private final RiskScoreRepository riskRepo;
    private final ValidatorRepository validatorRepo;

    @Override
    public Mono<RiskScore> calculate(String owner) {
        Mono<List<JailEvent>> jailEventsMono = jailRepo
                .findAllByValidatorOwner(owner)
                .collectList();

        Mono<Validator> validatorMono = validatorRepo
                .findByOwner(owner)
                .switchIfEmpty(Mono.error(new ValidatorNotFoundException(owner)));

        return Mono.zip(jailEventsMono, validatorMono)
                .flatMap(tuple -> {
                    List<JailEvent> jailEvents = tuple.getT1();
                    Validator validator = tuple.getT2();

                    double jailScore = computeJailScore(jailEvents);
                    double uptimeScore = computeUptimeScore(validator);
                    double skinScore = computeSkinScore(validator);

                    double total = props.getW1() * jailScore
                            + props.getW2() * uptimeScore
                            + props.getW3() * skinScore;

                    RiskScore rs = new RiskScore();
                    rs.setValidatorOwner(owner);
                    rs.setJailScore(jailScore);
                    rs.setUptimeScore(uptimeScore);
                    rs.setSkinScore(skinScore);
                    rs.setTotalScore(total);
                    rs.setTier(toTier(total));
                    rs.setModelVersion(CURRENT_RISK_MODEL_VERSION);
                    rs.setCalculatedAt(Instant.now());

                    return riskRepo.save(rs);
                });
    }

    private double computeJailScore(List<JailEvent> events) {
        double score = events.stream()
                .mapToDouble(e -> Math.exp(
                        -props.getLambda() * daysSince(e.getDetectedAt())
                ))
                .sum();
        return Math.min(100.0, score * props.getJailNorm());
    }

    private double computeUptimeScore(Validator v) {
        long totalOpportunities = v.getTotalValidatorSuccess()
                + v.getTotalValidatorFailure()
                + v.getTotalSignaturesIgnored();

        if (totalOpportunities == 0) return 0.0;

        double successRate = (double) v.getTotalValidatorSuccess() / totalOpportunities;
        return (1 - successRate) * 100;
    }

    private double computeSkinScore(Validator v) {
        if (v.getTotalStake() == 0) return 100.0;
        double skinRatio = (double) v.getSelfStake() / v.getTotalStake();
        return (1 - skinRatio) * 100;
    }

    private double daysSince(Instant instant) {
        return Duration.between(instant, Instant.now()).toHours() / 24.0;
    }

    private RiskTier toTier(double score) {
        if (score < 20) return RiskTier.LOW;
        if (score < 40) return RiskTier.MEDIUM;
        if (score < 65) return RiskTier.HIGH;
        return RiskTier.CRITICAL;
    }
}
