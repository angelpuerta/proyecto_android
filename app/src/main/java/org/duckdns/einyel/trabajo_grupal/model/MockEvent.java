package org.duckdns.einyel.trabajo_grupal.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "events")
public class MockEvent {


    @PrimaryKey(autoGenerate = true)
    private Long id;
    public double mark;
    public String description;
    public String tittle;
    public String location;

    @ColumnInfo(name = "imgURL")
    @SerializedName("imgURL")
    public String imgURLReal;

    public transient int imgURL;

    @Ignore
    private List<Comment> comments;


    public MockEvent(Long id) {
        this.id = id;
    }

    MockEvent(Long e_id, double mark, String description, String tittle, String location, String imgURL) {
        this.id = e_id;
        this.mark = mark;
        this.description = description;
        this.tittle = tittle;
        this.location = location;
        this.imgURLReal = imgURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImgURL() {
        return imgURL;
    }

    public void setImgURL(int imgURL) {
        this.imgURL = imgURL;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getImgURLReal() {
        return imgURLReal;
    }

    public void setImgURLReal(String imgURLReal) {
        this.imgURLReal = imgURLReal;
    }

    @Override
    public String toString() {
        return "MockEvent{" +
                "id=" + id +
                ", mark=" + mark +
                ", description='" + description + '\'' +
                ", tittle='" + tittle + '\'' +
                ", location='" + location + '\'' +
                ", imgURL=" + imgURL +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockEvent mockEvent = (MockEvent) o;
        return Objects.equals(id, mockEvent.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
