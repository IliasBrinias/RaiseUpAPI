package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.TagRequest;
import org.springframework.http.ResponseEntity;

public interface ITag {
    ResponseEntity<?> getTags();
    ResponseEntity<?> getTag(Long tagId);
    ResponseEntity<?> createTag(TagRequest request);

    ResponseEntity<?> editTag(Long tagId, TagRequest request);
    ResponseEntity<?> deleteTag(Long tagId);
}
