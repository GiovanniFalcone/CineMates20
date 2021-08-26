package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private List<String> cast;
    private Context context;

    public CastAdapter(Context context, List<String> cast){
        this.context = context;
        this.cast = cast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = cast.get(position);

        Picasso.get().load(url).transform(new CircleTransform()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.castImg);
        }
    }
}
