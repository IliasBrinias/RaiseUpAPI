package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.ITag;
import com.unipi.msc.raiseupapi.Model.Tag;
import com.unipi.msc.raiseupapi.Repository.TagRepository;
import com.unipi.msc.raiseupapi.Request.TagRequest;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.TagPresenter;
import com.unipi.msc.raiseupapi.Response.TaskPresenter;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagService implements ITag {
    private final TagRepository tagRepository;
    @Override
    public ResponseEntity<?> getTags() {
        List<TagPresenter> presenters = new ArrayList<>();
        tagRepository.findAll().forEach(tag-> presenters.add(TagPresenter.getPresenter(tag)));
        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> getTag(Long tagId) {
        List<TagPresenter> presenters = new ArrayList<>();
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag == null) return  GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        return GenericResponse.builder().data(TagPresenter.getPresenter(tag)).build().success();
    }

    @Override
    public ResponseEntity<?> createTag(TagRequest request) {
        Tag tag = tagRepository.save(Tag.builder()
            .name(request.getName())
            .color(request.getColor())
            .build());
        return GenericResponse.builder().data(TagPresenter.getPresenter(tag)).build().success();
    }

    @Override
    public ResponseEntity<?> editTag(Long tagId, TagRequest request) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag == null) return  GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        if (request.getColor()!=null){
            tag.setColor(request.getColor());
        }
        if (request.getName()!=null){
            tag.setName(request.getName());
        }
        tag = tagRepository.save(tag);
        return GenericResponse.builder().data(TagPresenter.getPresenter(tag)).build().success();
    }

    @Override
    public ResponseEntity<?> deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag == null) return  GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        tagRepository.delete(tag);
        return GenericResponse.builder().build().success();
    }

    @Override
    public ResponseEntity<?> searchTag(String keyword) {
        List<Tag> tags;
        if (keyword.isEmpty()){
            tags = tagRepository.findAll();
        }else {
            tags = tagRepository.findAllByNameContaining(keyword);
        }
        List<TagPresenter> presenter = TagPresenter.getPresenter(tags);
        return GenericResponse.builder().data(presenter).build().success();
    }
}
