package com.unipi.msc.raiseupapi.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Long dueTo;
    private Difficulty difficulty;
    private boolean completed;
    @ManyToOne
    private Step step;
    @ManyToMany(mappedBy = "tasks")
    private List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @ManyToMany
    private List<Tag> tags = new ArrayList<>();
}