package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.ColumnRequest;
import org.springframework.http.ResponseEntity;

public interface IStep {
    ResponseEntity<?> editTag(Long columnId, ColumnRequest request);
}
