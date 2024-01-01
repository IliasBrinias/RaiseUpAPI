package com.unipi.msc.riseupapi.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Long date;

    @ManyToOne
    private Task task;
}