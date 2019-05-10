package org.duckdns.einyel.trabajo_grupal.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private Long id;

    private String nick;

    private String password;

    private String sexo;

    private String fbId = "";

    private String twUsername = "";

    private String pfpUrl = "";

    private String nacimiento = "";

    public User(Long id, String nick, String password, String sexo) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
    }

    public User(Long id, String nick, String password, String sexo, String fecha) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.sexo = sexo;
        this.nacimiento = fecha;
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
    }

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
}
