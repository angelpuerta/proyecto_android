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
        if(loc.length!=2 || (loc[0].equals("0") && loc[1].equals("0")))
            return false;
        String numeric_pattern = "-?\\d+(\\.\\d+)?";
        return loc[0].trim().matches(numeric_pattern) && loc[1].trim().matches(numeric_pattern);
    }

    public static boolean code(String code){
        return code!=null && !code.isEmpty();
    }

    public static boolean email(String email){
        return email!=null && !email.isEmpty() && REGEX.matcher(email.trim()).matches();
    }


}

