package org.duckdns.einyel.trabajo_grupal.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private Long id;

    private String nick;

    private String password;

    public User(Long id, String nick, String password) {
        this.id = id;
        this.nick = nick;
        this.password = password;
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
}
