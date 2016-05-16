package sk.leafe.android.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import sk.leafe.android.R;
import sk.leafe.android.models.Note;

public class TextNoteActivity extends AppCompatActivity {
    public static String TAG = "TextNoteActivity";
    private EditText mTitle, mContent;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.content);
        String newString, newString2;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
                newString2= null;
            } else {
                newString= extras.getString("content");
                newString2= extras.getString("title");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("content");
            newString2= (String) savedInstanceState.getSerializable("title");
        }
        mTitle.setText(newString2);
        mContent.setText(newString);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void onSave(View view) {
        Log.d(TAG, "click");

        String title = mTitle.getText().toString();

        Log.d(TAG, title);

        // otvorenie suboru do externej pameti s title, to je meno suboru a nadpis ktory davas v tej appke

        File file = new File (path + "/"+title+".txt");

        // text sa da na pole stringou, toto sa nacita z toho textoveho pola v appke
        String [] saveText = String.valueOf(mContent.getText()).split(System.getProperty("line.separator"));

        Save (file, saveText);

        finish();
    }


    // Funkcia ktora uklada do suboru
    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            for (int i = 0; i < data.length; i++) {
                fos.write(data[i].getBytes());
                if (i < data.length - 1) {
                    fos.write("\n".getBytes());
                }
            }

            fos.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    // funkcia ktora nacitava a vracia zo suboru
    public static String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }
}
