package com.unipi.msc.raiseupapi.Model;

import com.unipi.msc.raiseupapi.Model.User.User;
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
    private Long dueTo;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}