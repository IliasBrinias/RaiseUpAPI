package com.unipi.msc.raiseupapi.Request;

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
    private List<Long> employeesId = new ArrayList<>();
    private List<String> columns = new ArrayList<>();
}
