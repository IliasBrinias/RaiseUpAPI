package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Shared.Tags;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class GenericResponse<T> {
    private int code;
    private String message;
    private T data;

    public ResponseEntity<?> success(){
        this.code = Tags.HTTP_OK;
        return ResponseEntity.ok(this);
    }

    public ResponseEntity<?> badRequest(){
        this.code = Tags.HTTP_BAD_REQUEST;
        return ResponseEntity.badRequest().body(this);
    }
    public ResponseEntity<?> accessDenied(){
        this.code = Tags.HTTP_ACCESS_DENIED;
        this.message = Tags.ACCESS_DENIED;
        return ResponseEntity.badRequest().body(this);
    }
    public ResponseEntity<?> response(){
        return ResponseEntity.badRequest().body(this);
    }

}