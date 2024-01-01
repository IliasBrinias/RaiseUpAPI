package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.TagRequest;
import org.springframework.http.ResponseEntity;

public interface ITag {
    ResponseEntity<?> getTags();
    ResponseEntity<?> getTag(Long tagId);
    ResponseEntity<?> createTag(TagRequest request);

    ResponseEntity<?> editTag(Long tagId, TagRequest request);
    ResponseEntity<?> deleteTag(Long tagId);

    ResponseEntity<?> searchTag(String keyword);
}
