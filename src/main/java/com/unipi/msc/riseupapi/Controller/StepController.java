package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IStep;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("step")
@RequiredArgsConstructor
public class StepController {

    private final IStep iStep;
    @PatchMapping("{stepId}")
    public ResponseEntity<?> editTag(@PathVariable Long stepId, @RequestBody ColumnRequest request){
        return iStep.editTag(stepId,request);
    }
}
