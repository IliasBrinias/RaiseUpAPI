package com.unipi.msc.riseupapi.Request;

import jakarta.persistence.NamedStoredProcedureQueries;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsRequest {
    private Long dateFrom;
    private Long dateTo;
}
