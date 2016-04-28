package sk.leafe.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.leafe.android.App;
import sk.leafe.android.R;
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

        Note item = mValues.get(position);

        holder.mItem = item;
        holder.mTitleText.setText(item.title);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open new activity
                // temporary set note to global app
//                App.note = holder.mItem;

//                Intent intent = new Intent(mContext, NoteActivity.class);
//                intent.putExtra("id", holder.mItem.na + "");
//                mContext.startActivity(intent);
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

        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleText = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleText.getText() + "'";
        }
    }
}
