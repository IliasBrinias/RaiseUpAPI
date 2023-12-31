package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.ColumnRequest;
import org.springframework.http.ResponseEntity;

public interface IStep {
    ResponseEntity<?> editTag(Long columnId, ColumnRequest request);
}
