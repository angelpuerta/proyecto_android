package org.duckdns.einyel.trabajo_grupal.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "events")
public class Event implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private Long id;
    public double mark;
    public String description;
    public String tittle;
    public String location;

    public String imgURL;
    public int numberOfComments;
    public String tags;


    @Ignore
    private String code;



    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long e_id, double mark, String description, String tittle, String location, String imgURL) {
        this.id = e_id;
        this.mark = mark;
        this.description = description;
        this.tittle = tittle;
        this.location = location;
        this.imgURL = imgURL;
    }

    public Event(Long id, double mark, String description, String tittle, String location, String imgURL, int numberOfComments, String tags, String code) {
        this(id,mark,description,tittle, location, imgURL);
        this.numberOfComments = numberOfComments;
        this.tags = tags;

    }

    protected Event(Parcel in) {
        id = in.readLong();
        tittle = in.readString();
        description = in.readString();
        imgURL = in.readString();
        mark = in.readDouble();
        location = in.readString();
        numberOfComments = in.readInt();
        tags = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event mockEvent = (Event) o;
        return Objects.equals(id, mockEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.tittle);
        dest.writeString(this.description);
        dest.writeString(this.imgURL);
        dest.writeDouble(this.mark);
        dest.writeString(this.location);
        dest.writeInt(this.numberOfComments);
        dest.writeString(this.tags);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
