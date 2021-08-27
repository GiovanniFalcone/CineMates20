package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewUserAdapter extends RecyclerView.Adapter<ReviewUserAdapter.ViewHolder>{

    final private Context context;
    final private List<Review> authorList;
    //interface for click item
    private ClickListener clickListener;

    public ReviewUserAdapter(Context context, List<Review> authorList) {
        this.context = context;
        this.authorList = authorList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView propic;
        private final FloatingActionButton button;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.txtName);
            button = v.findViewById(R.id.imageView5);
            propic = v.findViewById(R.id.imageView7);
        }
    }

    @NonNull
    @Override
    public ReviewUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ReviewUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewUserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Review authorReview = authorList.get(position);

        holder.title.setText(authorReview.getAuthor());
        holder.title.setTextColor(ContextCompat.getColor(context, R.color.white));


        UserDAO userDAO = new UserDAO_Firestore(context.getApplicationContext());
        Uri uri = userDAO.getImageUri(authorReview.getAuthor());
        if(uri != null){
            Picasso.get().load(uri).transform(new CircleTransform()).into(holder.propic);
        }

        holder.itemView.setOnClickListener(view -> clickListener.onItemClickListener(authorList, position, view));
        holder.button.setOnClickListener(view -> clickListener.onItemClickListener(authorList, position, view));
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(List<Review> authorList, int position, View view);
    }
}
