package sk.leafe.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

/**
 * Created by QBIT on 09/03/16.
 */
public class App extends Application {
    public static final String TAG = "App";

    public static Context context;

    // policka na obrazok
    public static Bitmap bm;

    public App() {
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init internal db
        ActiveAndroid.initialize(this);
        Log.i(TAG, "App initialized");

        String test = "moj test";

        String dalsi = test;
    }

    public static boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(context, perm));
    }
}