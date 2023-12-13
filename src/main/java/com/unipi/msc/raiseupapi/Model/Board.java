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
    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Step> columns = new ArrayList<>();
}