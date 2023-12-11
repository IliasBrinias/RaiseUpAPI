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
public class ProjectTag {
    @Id
    @GeneratedValue
    private Long id;
    private String tag;
    @OneToMany(mappedBy = "projectTag", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProjectTagLink> projectTagLinks = new ArrayList<>();
}
