package org.duckdns.einyel.trabajo_grupal.service;

import com.twitter.Regex;

import java.util.regex.Pattern;

public class Check {

    private static final String EMAIL_VERIFICATION = "^([\\w-.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    private static final Pattern REGEX = Pattern.compile(EMAIL_VERIFICATION);

    public static boolean location(String location){
        if(location == null || location.isEmpty())
            return false;
        String[] loc = location.split(",");
        String numeric_pattern = "-?\\d+(\\.\\d+)?";
        boolean uno = loc[0].matches(numeric_pattern);
        boolean dos = loc[1].matches(numeric_pattern);
        return loc.length == 2 && loc[0].trim().matches(numeric_pattern) && loc[1].trim().matches(numeric_pattern);
    }

    public static boolean code(String code){
        return code!=null && !code.isEmpty();
    }

    public static boolean email(String email){
        return email!=null && !email.isEmpty() && REGEX.matcher(email.trim()).matches();
    }


}

