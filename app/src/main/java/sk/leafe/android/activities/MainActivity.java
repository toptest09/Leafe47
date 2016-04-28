package sk.leafe.android.activities;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import sk.leafe.android.R;
import sk.leafe.android.adapters.NoteRecyclerViewAdapter;
import sk.leafe.android.models.Note;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NoteRecyclerViewAdapter mAdapter;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text_note) {

            // spusti
            Intent intent = new Intent(this, TextNoteActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}