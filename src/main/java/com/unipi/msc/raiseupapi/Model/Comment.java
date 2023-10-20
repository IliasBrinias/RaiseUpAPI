package com.unipi.msc.raiseupapi.Model;

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
    private Long timestamp;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

}