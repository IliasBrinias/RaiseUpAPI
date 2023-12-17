package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentPresenter {
    private Long id;
    private String message;
    private Long timestamp;
    public static CommentPresenter getPresenter(Comment comment){
        return CommentPresenter.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .timestamp(comment.getTimestamp())
                .build();
    }
    public static List<CommentPresenter> getPresenter(List<Comment> comments){
        List<CommentPresenter> presenters = new ArrayList<>();
        comments.forEach(comment -> presenters.add(getPresenter(comment)));
        return presenters;
    }
}
