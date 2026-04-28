package org.shieldx.oracle.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "risk-score")
public class RiskScoreProperties {
    private double w1, w2, w3, w4;
    private double lambda;
    private double jailNorm;
}

