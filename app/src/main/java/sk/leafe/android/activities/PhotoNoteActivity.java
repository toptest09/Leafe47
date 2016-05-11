package sk.leafe.android.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import sk.leafe.android.App;
import sk.leafe.android.R;

public class PhotoNoteActivity extends AppCompatActivity {
    public static String TAG = "PhotoNoteActivity";
    private EditText mTitle;
    private ImageView mPhoto;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = (EditText) findViewById(R.id.title);
        mPhoto = (ImageView) findViewById(R.id.photo);
        String newString, newString2;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

                newString2= null;
            } else {
                newString2= extras.getString("title");
            }
        } else {
            newString2= (String) savedInstanceState.getSerializable("title");
        }
        EditText title = (EditText) this.findViewById(R.id.title);
        title.setText(newString2);
        File dir = new File(path);
        dir.mkdirs();

        // ak nie je policka prazna, vyber obrazok a zobraz
        if(App.bm != null) {
            mPhoto.setImageBitmap(App.bm);
        }
    }

    public void onSave(View view) {
        Log.d(TAG, "click");

        String title = mTitle.getText().toString();

        Log.d(TAG, title);

        // otvorenie suboru do externej pameti s title, to je meno suboru a nadpis ktory davas v tej appke

        File file = new File (path + "/"+title+".jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        App.bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();

        FileOutputStream fo;
        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(byteArray);
            fo.close();

            Toast toast = Toast.makeText(PhotoNoteActivity.this, "Obrazok ulozeny", Toast.LENGTH_LONG);
            toast.show();

            // upload file

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();
    }


    // Funkcia ktora uklada do suboru
    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
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
