package org.duckdns.einyel.trabajo_grupal.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    public String contact;
    public String date;

    private static SimpleDateFormat dateTimeFormater = new SimpleDateFormat("dd/MM/yy HH:mm");


    @Ignore
    private String code;


    public Event() {
    }


    protected Event(Parcel in) {
        id = in.readLong();
        tittle = in.readString();
        description = in.readString();
        imgURL = in.readString();
        mark = in.readDouble();
        location = in.readString();
        tags = in.readString();
        numberOfComments = in.readInt();
        date = in.readString();
        contact = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.tittle);
        dest.writeString(this.description);
        dest.writeString(this.imgURL);
        dest.writeDouble(this.mark);
        dest.writeString(this.location);
        dest.writeString(this.tags);
        dest.writeInt(this.numberOfComments);
        dest.writeString(this.date);
        dest.writeString(this.contact);
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
        return "Event{" +
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
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String formatedDate(){
        if(getDate()==null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
       return day + "-" + month + "-" + year +" "+ hour +":"+minutes;
    }

    public Date getDate(){
        try {
            return formatDate(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date formatDate(String date) throws ParseException {
        return date!= null? dateTimeFormater.parse(date) : null;
    }
}
