package dev.lfspersson.a4all.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.activities.ItemDetailActivity_;

/**
 * Created by LFSPersson on 18/11/16.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private List<String> items;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitleItem;
        public LinearLayout llItem;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvTitleItem = (TextView) v.findViewById(R.id.tvItemTitle);
            llItem = (LinearLayout) v.findViewById(R.id.llItem);
        }
    }

    public ItemListAdapter(List<String> myDataset, Context c) {
        items = myDataset;
        context = c;
    }

    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.item_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String id = items.get(position);
        holder.tvTitleItem.setText(id);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).extra("itemId", id).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}