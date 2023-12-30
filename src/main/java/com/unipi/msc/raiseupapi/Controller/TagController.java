package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.ITag;
import com.unipi.msc.raiseupapi.Request.TagRequest;
import com.unipi.msc.raiseupapi.Response.TaskPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
public class TagController {

    private final ITag iTag;
    @GetMapping
    public ResponseEntity<?> getTags(){
        return iTag.getTags();
    }

    @GetMapping("{tagId}")
    public ResponseEntity<?> getTag(@PathVariable Long tagId){
        return iTag.getTag(tagId);
    }
    @GetMapping("search")
    public ResponseEntity<?> getTag(@RequestParam String keyword){
        return iTag.searchTag(keyword);
    }
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagRequest request){
        return iTag.createTag(request);
    }
    @PatchMapping("{tagId}")
    public ResponseEntity<?> editTag(@RequestBody TagRequest request, @PathVariable Long tagId){
        return iTag.editTag(tagId,request);
    }
    @DeleteMapping("{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId){
        return iTag.deleteTag(tagId);
    }

}
