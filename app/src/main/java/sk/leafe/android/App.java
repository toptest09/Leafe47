package sk.leafe.android;

import android.app.Application;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

/**
 * Created by QBIT on 09/03/16.
 */
public class App extends Application {
    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        // init internal db
        ActiveAndroid.initialize(this);

        Log.i(TAG, "App initialized");
    }
}