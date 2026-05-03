package org.shieldx.oracle.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.shieldx.oracle.api.dto.PageResponse;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;
import org.shieldx.oracle.api.dto.validator.ValidatorSummaryDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import static org.shieldx.oracle.api.Constants.API_V1;
import static org.shieldx.oracle.api.Constants.VALIDATORS;


@RequestMapping(API_V1 + VALIDATORS)
@Tag(name = Constants.OpenApi.VALIDATORS)
public interface ValidatorOperation {

    @GetMapping
    @Operation(
            summary = "Get validators list",
            description = "Returns paginated list of validators with risk scores"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Validators fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    Mono<PageResponse<ValidatorSummaryDto>> getValidators(@Parameter(in = ParameterIn.QUERY, explode = Explode.TRUE) @ParameterObject ValidatorFilter filter);
}
