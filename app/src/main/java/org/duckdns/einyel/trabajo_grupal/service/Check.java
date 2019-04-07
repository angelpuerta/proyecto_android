package org.duckdns.einyel.trabajo_grupal.service;

public class Check {

    public static boolean location(String location){
        String[] loc = location.split(",");
        String numeric_pattern = "-?\\d+(\\.\\d+)?";
        if(loc.length != 2 || !loc[0].matches(numeric_pattern) || !loc[1].matches(numeric_pattern))
            return false;
        return true;
    }


}

