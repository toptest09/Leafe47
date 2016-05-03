package sk.leafe.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import sk.leafe.android.App;
import sk.leafe.android.R;
import sk.leafe.android.adapters.NoteRecyclerViewAdapter;
import sk.leafe.android.models.Note;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NoteRecyclerViewAdapter mAdapter;

    static final int REQUEST_CAMERA = 200;
    static final int SELECT_FILE = 201;

    List<Note> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new NoteRecyclerViewAdapter(mList, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("MAIN", "aktivita je aktivna");

        File dir = new File(TextNoteActivity.path); // cesta k poznamkam
        File[] files = dir.listFiles();

        if(files == null) {
            return;
        }

        // vymaze vsetky zaznamy z listu
        mList.clear();

        for (File file : files) { // opakuje sa na vsetky subory
            if (!file.isDirectory()) {
                Note note1 = new Note();
                note1.title = file.getName().split("\\.")[0]; // mena poznamok
                note1.content = TextUtils.join(" \n ",TextNoteActivity.Load(file)); // obsah poznamomk
                note1.type = file.getName().split("\\.")[1].equals("jpg") ? "photo" : "text";

                mList.add(note1); // prida poznamku
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // ak user klikol na textovu poznamku
        if (id == R.id.action_text_note) {

            // spusti
            Intent intent = new Intent(this, TextNoteActivity.class);
            startActivity(intent);

            return true;
        }

        // ak user klikol na fotku
        if (id == R.id.action_photo_note) {

            final CharSequence[] items = { "Urobit novu fotku", "Vybrat fotku z galerie" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nova poznamka s foktou");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Urobit novu fotku")) {

                        if(App.hasPermission(Manifest.permission.CAMERA)) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            Toast toast = Toast.makeText(MainActivity.this, "Error: Camera permission required", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    } else if (items[item].equals("Vybrat fotku z galerie")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // metoda ktora zachyti vysledok aktivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pozreme ci je vsetko ok
        if (resultCode == RESULT_OK) {

            // bolo to z fotaku
            if (requestCode == REQUEST_CAMERA) {

                Bitmap bm = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                // resize and compress
                bm = getResizedBitmap(bm, 1200);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                // ulozim na policku
                App.bm = bm;

                // spustim novu aktivitu
                Intent intent = new Intent(this, PhotoNoteActivity.class);
                startActivity(intent);


//                // create and save new file for upload
//                File file = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
//
//                FileOutputStream fo;
//                try {
//                    file.createNewFile();
//                    fo = new FileOutputStream(file);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//
//                    Toast toast = Toast.makeText(MainActivity.this, "Obrazok ulozeny", Toast.LENGTH_LONG);
//                    toast.show();
//
//                    // upload file
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                // set bitmap
//                mPhotoView.setImageBitmap(bm);

                // bolo to z galerie
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, this);

                if (tempPath == null || tempPath.isEmpty()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Error: Could not process this image", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                Bitmap bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

                // resize and compress
                bm = getResizedBitmap(bm, 1200);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                // ulozim na policku
                App.bm = bm;

                // spustim novu aktivitu
                Intent intent = new Intent(this, PhotoNoteActivity.class);
                startActivity(intent);


//                // create and save new file for upload
//                File file = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
//
//                FileOutputStream fo;
//                try {
//                    file.createNewFile();
//                    fo = new FileOutputStream(file);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//
//                    // upload photo
//                    Toast toast = Toast.makeText(MainActivity.this, "Obrazok ulozeny", Toast.LENGTH_LONG);
//                    toast.show();
//
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                // set bitmap
//                // mPhotoView.setImageBitmap(bm);
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        if(image == null) {
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}