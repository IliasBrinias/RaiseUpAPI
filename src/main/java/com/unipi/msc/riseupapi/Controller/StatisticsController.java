package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final IStatistics iStatistics;
    @GetMapping
    public ResponseEntity<?> getStatistics(@RequestParam Long dateFrom, @RequestParam Long dateTo){
        return iStatistics.getStatistics(dateFrom, dateTo);
    }
    @GetMapping("user")
    public ResponseEntity<?> getUsersStatistics(@RequestParam Long dateFrom, @RequestParam Long dateTo){
        return iStatistics.getUsersStatistics(dateFrom, dateTo);
    }
}
