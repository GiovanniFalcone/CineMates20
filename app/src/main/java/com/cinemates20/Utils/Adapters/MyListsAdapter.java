package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinemates20.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyListsAdapter extends RecyclerView.Adapter<MyListsAdapter.ViewHolder> {

    private Context context;
    private List<String> nameLists;
    //interface for click item
    private ClickListener clickListener;

    public MyListsAdapter(Context context, List<String> nameLists) {
        this.context = context;
        this.nameLists = nameLists;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameListView;
        private ImageView seeMovieListImage;

        public ViewHolder(View v) {
            super(v);
            nameListView = v.findViewById(R.id.txtNameList);
            seeMovieListImage = v.findViewById(R.id.seeList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_lists_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nameList = nameLists.get(position);

        holder.nameListView.setText(nameList);
        holder.seeMovieListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClickListener(nameList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameLists.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(String listClicked);
    }
}
