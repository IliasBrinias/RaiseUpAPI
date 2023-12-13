package com.unipi.msc.raiseupapi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "tag", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProjectTagLink> projectTagLinks = new ArrayList<>();
}
