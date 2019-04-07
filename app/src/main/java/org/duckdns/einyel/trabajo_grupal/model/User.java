package org.duckdns.einyel.trabajo_grupal.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private Long id;

    private String nick;

    private String password;

    private List<Long> assisted;

    public User(Long id, String nick, String password) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.assisted = new ArrayList<>();
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

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public void addAssited(DataSnapshot assistedEvents){
        for (DataSnapshot event : assistedEvents.getChildren()){
            assisted.add(Long.parseLong(event.getValue().toString()));
        }
    }


}
