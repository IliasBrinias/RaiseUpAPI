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
public class Board {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Long date;
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Step> steps = new ArrayList<>();

    @ManyToMany(mappedBy = "boards")
    private List<User> users = new ArrayList<>();
    @ManyToOne
    private User owner;
}