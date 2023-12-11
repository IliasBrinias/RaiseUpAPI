package com.unipi.msc.raiseupapi.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "EMPLOYEE")
public class Employee extends User{
    @ManyToMany
    @JoinTable(name = "Employee_tasks",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "tasks_id"))
    private List<Task> tasks = new ArrayList<>();
}
