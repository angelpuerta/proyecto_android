package org.duckdns.einyel.trabajo_grupal;

import java.util.ArrayList;
import java.util.List;

public class MockEvent {

    private List<String> comments;
    private Long id;
    private List<Float> marks;
    public String description;
    public String tittle;
    public String location;
    public int imgURL;


    public MockEvent(Long id) {
        this.comments = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.id = id;
    }

    public void addComment(String comment){
        this.comments.add(comment);
    }

    public void addMark(Float mark){
        this.marks.add(mark);
    }

    public void addMarkAndComment(String comment, Float mark){
        addComment(comment);
        addMark(mark);
    }

    public String getDescription() {
        return description;
    }

    public String getTittle() {
        return tittle;
    }

    public String getLocation() {
        return location;
    }

    public int getImgURL() {
        return imgURL;
    }

    @Override
    public String toString() {
        return "MockEvent{" +
                "comment='" + comments + '\'' +
                ", id=" + id +
                ", puntuation=" + marks +
                '}';
    }

    public Long getId() {
        return id;
    }
}
