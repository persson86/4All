package dev.lfspersson.a4all.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.activities.ItemDetailActivity_;
import dev.lfspersson.a4all.activities.MainActivity;
import dev.lfspersson.a4all.database.models.ItemModel;

/**
 * Created by LFSPersson on 18/11/16.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private List<ItemModel> items;
    private Context context;
    private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFotoItem;
        public TextView tvTitleItem;
        public TextView tvCidadeItem;
        public TextView tvBairroItem;
        public LinearLayout llItem;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            ivFotoItem = (ImageView) v.findViewById(R.id.ivItemFoto);
            tvTitleItem = (TextView) v.findViewById(R.id.tvItemTitle);
            tvCidadeItem = (TextView) v.findViewById(R.id.tvItemCidade);
            tvBairroItem = (TextView) v.findViewById(R.id.tvItemBairro);
            llItem = (LinearLayout) v.findViewById(R.id.llItem);
        }
    }

    public ItemListAdapter(List<ItemModel> myDataset, Context c, Activity a) {
        items = myDataset;
        context = c;
        activity = a;
    }

    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String id = items.get(position).getId();

        holder.tvTitleItem.setText(items.get(position).getTitulo());
        holder.tvCidadeItem.setText(items.get(position).getCidade());
        holder.tvBairroItem.setText(items.get(position).getBairro());

        Glide.with(holder.ivFotoItem.getContext())
                .load(items.get(position).getUrlFoto())
                .into(holder.ivFotoItem);

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).extra("itemId", id).start();
                activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}