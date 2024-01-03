package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.StatisticsRequest;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface IStatistics {
    ResponseEntity<?> getStatistics(StatisticsRequest request);

    ResponseEntity<?> getUsersStatistics(StatisticsRequest request);

    ResponseEntity<?> getUserStatistics(Long userId, StatisticsRequest request);
}
