package sk.leafe.android.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.leafe.android.App;
import sk.leafe.android.R;
import sk.leafe.android.activities.TextNoteActivity;
import sk.leafe.android.models.Note;

/**
 * Created by QBIT on 09/03/16.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "TopicAdapter";
    private final List<Note> mValues;
    private final Context mContext;

    public NoteRecyclerViewAdapter(List<Note> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Note item = mValues.get(position);

        holder.mItem = item;
        holder.mTitleText.setText(item.title);


        // tato funkcia sa spusti ked kliknes na item
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open new activity
                // temporary set note to global app
//                App.note = holder.mItem;

                // TODO: check for note type

                Intent intent = new Intent(mContext, TextNoteActivity.class);
                intent.putExtra("title", holder.mItem.title + "");
                mContext.startActivity(intent);
//
//                Toast toast = Toast.makeText(mContext, "Klikol si na poznamku", Toast.LENGTH_SHORT);
//                toast.show();
            }
        });

        // tato funckia sa spusti ked kliknes na delete button
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast toast = Toast.makeText(mContext, "Klikol si na zmazat", Toast.LENGTH_SHORT);
//                toast.show();

                // vytvorim alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // nastavim title pre dialog
                builder.setMessage("Naozaj chces zmazat tuto poznamku ?")
                        // ano tlacitlo
                        .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // sem patri kod co vymaze poznamku

                                // toto je objekt poznamky


                                Toast toast = Toast.makeText(mContext, item.title, Toast.LENGTH_SHORT);
                                toast.show();

                                // TODO: vlozit kod na mazanie poznamky

//                                mValues.remove(item);
//                                mValues.notifyAll();
                            }
                        })
                        // zrusit tlacidlo
                        .setNegativeButton("Zrusit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleText;
        public final ImageView mDeleteButton;

        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleText = (TextView) view.findViewById(R.id.title);
            mDeleteButton = (ImageView) view.findViewById(R.id.delete_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleText.getText() + "'";
        }
    }
}
