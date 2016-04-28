package sk.leafe.android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by QBIT on 09/03/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    public SharedPreferences.Editor mSharedPreferencesEditor;
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        configureSharedPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void configureSharedPreferences() {
        Log.d(TAG, "Configuring shared preferences");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = this.mSharedPreferences.edit();
    }
}
