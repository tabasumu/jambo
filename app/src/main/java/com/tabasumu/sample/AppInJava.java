package com.tabasumu.sample;

import android.app.Application;

import com.tabasumu.jambo.Jambo;

/**
 * Jambo
 *
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/18/22 at 3:15 PM
 */
public class AppInJava extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        plantJambo();

    }

    private void plantJambo() {
        new Jambo.Builder(this)
                .enableNotifications(true)
                .build();
    }

}
