package com.unipi.msc.riseupapi.Interface;

import org.springframework.http.ResponseEntity;

public interface IStatistics {
    ResponseEntity<?> getStatistics(Long dateFrom, Long dateTo);

    ResponseEntity<?> getUsersStatistics(Long dateFrom, Long dateTo);

    ResponseEntity<?> getUserStatistics(Long userId, Long dateFrom, Long dateTo);
}
