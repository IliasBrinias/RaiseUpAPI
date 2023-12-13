package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Tag;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagPresenter {
    private Long id;
    private String name;
    private String color;
    public static TagPresenter getPresenter(Tag tag){
        return TagPresenter.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}
