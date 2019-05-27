package org.duckdns.einyel.trabajo_grupal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.duckdns.einyel.trabajo_grupal.model.User;

public abstract class UserHolder extends Service{
    public static User user;
    public static String socialLogin = "";

    public static User getUser(){
        return user;
    }

    public static void setUser(User user){
        UserHolder.user = user;
    }

    public static String getSocialLogin(){
        return socialLogin;
    }

    public static void setSocialLogin(String socialLogin){
        UserHolder.socialLogin = socialLogin;
    }
}
