package dev.lfspersson.a4all.adapters;

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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import dev.lfspersson.a4all.R;
import dev.lfspersson.a4all.activities.ItemDetailActivity_;
import dev.lfspersson.a4all.models.ItemComentarioModel;

/**
 * Created by LFSPersson on 23/11/16.
 */

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {
    private List<ItemComentarioModel> comentarioList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNome;
        public TextView tvTitulo;
        public TextView tvComentario;
        public ImageView ivFoto;
        public LinearLayout llComentario;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvNome = (TextView) v.findViewById(R.id.tvComentarioNome);
            tvTitulo = (TextView) v.findViewById(R.id.tvComentarioTitulo);
            tvComentario = (TextView) v.findViewById(R.id.tvComentarioComentario);
            ivFoto = (ImageView) v.findViewById(R.id.ivFoto);
            //llComentario = (LinearLayout) v.findViewById(R.id.llComentario);
        }
    }

    public ComentarioAdapter(List<ItemComentarioModel> myDataset, Context c) {
        comentarioList = myDataset;
        context = c;
    }

    @Override
    public ComentarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.comentario_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvNome.setText(comentarioList.get(position).getNome());
        holder.tvTitulo.setText(comentarioList.get(position).getTitulo());
        holder.tvComentario.setText(comentarioList.get(position).getComentario());
        loadFotoImage(comentarioList.get(position).getUrlFoto(), holder.ivFoto);
    }

    @Override
    public int getItemCount() {
        return comentarioList.size();
    }

    public void loadFotoImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //pbFoto.setVisibility(View.GONE);
                        //ivFoto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

}