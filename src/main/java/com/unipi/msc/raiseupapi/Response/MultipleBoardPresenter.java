package com.unipi.msc.raiseupapi.Response;

import java.util.ArrayList;
import java.util.List;

public class MultipleBoardPresenter {
    private Long id;
    private String title;
    private Long date;
    private List<String> profiles = new ArrayList<>();
    private Long numberOfEmployees;
    private Long totalTasks;

    public MultipleBoardPresenter(Long id, String title, Long date, List<String> profiles, Long numberOfEmployees, Long totalTasks) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.profiles = profiles;
        this.numberOfEmployees = numberOfEmployees;
        this.totalTasks = totalTasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public Long getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Long numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }
}
