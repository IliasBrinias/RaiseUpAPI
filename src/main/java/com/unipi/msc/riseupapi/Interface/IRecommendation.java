package com.unipi.msc.riseupapi.Interface;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRecommendation {
    void train();
    List<Long> recommendUsers(Long tagId, Long difficultyId);
}
