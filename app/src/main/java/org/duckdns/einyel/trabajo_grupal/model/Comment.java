package org.duckdns.einyel.trabajo_grupal.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "comments",
        indices = {@Index(value = {"u_id", "timestamp"},
                unique = true), @Index(value = "e_id")}
        , foreignKeys = {
        @ForeignKey(
                entity = Event.class,
                parentColumns = "id",
                childColumns = "e_id",
                onDelete = CASCADE),
       /* @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "u_id",
                onDelete = CASCADE)*/
})
@TypeConverters(DateConverter.class)
public class Comment {

    @SerializedName("c_id")
    @PrimaryKey(autoGenerate = true)
    private Long c_id;

    @SerializedName("e_id")
    @ColumnInfo(name = "e_id")
    private Long e_id;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rate")
    private double rate;

    @SerializedName("user_icon_left")
    private String user;

    @Ignore
    @SerializedName("u_id")
    @ColumnInfo(name = "u_id")
    private Long u_id;

    @SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    private Date timestamp;

    public Comment() {

    }

    @Ignore
    public Comment(Long e_id, String comment, double rate) {
        this.e_id = e_id;
        this.comment = comment;
        this.rate = rate;
        this.timestamp = new Date();
        this.u_id = 0L;
        this.c_id = new Long(hashCode());
        this.user = "anonymous";
    }


    public Comment(Long e_id, String comment, double rate, String user) {
        this(e_id, comment, rate);
        this.setUser(user);
    }

    public Long getC_id() {
        return c_id;
    }

    public void setC_id(Long c_id) {
        this.c_id = c_id;
    }

    public Long getE_id() {
        return e_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRate() {
        return rate;
    }

    public Long getU_id() {
        return u_id;
    }

    public void setU_id(Long u_id) {
        this.u_id = u_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setE_id(Long e_id) {
        this.e_id = e_id;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        if (user == null || user.equals(""))
            this.user = "anonymous";
        else
            this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "c_id=" + c_id +
                ", e_id=" + e_id +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                ", u_id=" + u_id +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(e_id, comment.e_id) &&
                Objects.equals(u_id, comment.u_id) &&
                Objects.equals(timestamp, comment.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(e_id, u_id, timestamp);
    }
}
