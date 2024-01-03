package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IAuth;
import com.unipi.msc.riseupapi.Interface.IStatistics;
import com.unipi.msc.riseupapi.Request.LoginRequest;
import com.unipi.msc.riseupapi.Request.RegisterRequest;
import com.unipi.msc.riseupapi.Request.StatisticsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final IStatistics iStatistics;
    @GetMapping
    public ResponseEntity<?> getStatistics(@RequestBody StatisticsRequest request){
        return iStatistics.getStatistics(request);
    }
    @GetMapping("user")
    public ResponseEntity<?> getUsersStatistics(@RequestBody StatisticsRequest request){
        return iStatistics.getUsersStatistics(request);
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUserStatistics(@PathVariable Long userId, @RequestBody StatisticsRequest request){
        return iStatistics.getUserStatistics(userId, request);
    }

}
