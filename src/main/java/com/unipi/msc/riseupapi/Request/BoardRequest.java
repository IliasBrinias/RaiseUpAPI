package com.unipi.msc.riseupapi.Request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequest {
    private String title;
    private List<Long> employeesId;
    private List<String> columns;
}
