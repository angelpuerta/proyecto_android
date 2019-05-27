package org.duckdns.einyel.trabajo_grupal.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "users")
public class User implements Parcelable {

    @PrimaryKey
    private Long id;

    private String nick;

    private String password;

    private String sexo;

    private String fbId = "";

    private String twUsername = "";

    private String pfpUrl = "";

    private String nacimiento = "";

    private List<Long> assisted;

    public User(Long id, String nick, String password, String sexo) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
        assisted = new ArrayList<>();
    }

    public User(Long id, String nick, String password, String sexo, String fecha) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
        this.nacimiento = fecha;
        assisted = new ArrayList<>();
    }

    public User(Long id, String nick, String password, String sexo, String fbId, String twUsername, String pfpUrl, String fecha) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
        this.fbId = fbId;
        this.twUsername = twUsername;
        this.pfpUrl = pfpUrl;
        this.nacimiento = fecha;
        assisted = new ArrayList<>();
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nick = in.readString();
        password = in.readString();
        assisted = new ArrayList<>();
        in.readList(assisted,Long.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(nick);
        dest.writeString(password);
        dest.writeList(assisted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public String getFbId() {
        return fbId;
    }

    public String getTwUsername() {
        return twUsername;
    }

    public String getPfpUrl() {
        return pfpUrl;
    }

    public void setPfpUrl(String pfpUrl) {
        this.pfpUrl = pfpUrl;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void setTwUsername(String twUsername) {
        this.twUsername = twUsername;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<Long> getAssisted() {
        return new ArrayList<>(assisted);
    }

    public void setAssisted(List<Long> assisted) {
        this.assisted = assisted;
    }

    public void addAssited(DataSnapshot assistedEvents){
        for (DataSnapshot event : assistedEvents.getChildren()){
            assisted.add(Long.parseLong(event.getValue().toString()));
        }
    }
}
