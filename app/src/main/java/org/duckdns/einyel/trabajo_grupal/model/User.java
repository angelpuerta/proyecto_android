package org.duckdns.einyel.trabajo_grupal.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private Long id;

    private String nick;

    private String password;

    private String sexo;

    private String fbId = "";

    private String twUsername = "";

    public User(Long id, String nick, String password, String sexo) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
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
}
