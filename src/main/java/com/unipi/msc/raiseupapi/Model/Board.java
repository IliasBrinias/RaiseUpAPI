package com.unipi.msc.raiseupapi.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    @OneToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Step> columns = new ArrayList<>();

}