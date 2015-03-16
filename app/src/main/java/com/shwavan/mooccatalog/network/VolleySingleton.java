package com.shwavan.mooccatalog.network;

/**
 * Created by GANGESHWAR on 24-02-2015.
 */
public class VolleySingleton {
    private static VolleySingleton ourInstance = new VolleySingleton();

    public static VolleySingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new VolleySingleton();
        }

        return ourInstance;
    }

    private VolleySingleton() {

    }
}
