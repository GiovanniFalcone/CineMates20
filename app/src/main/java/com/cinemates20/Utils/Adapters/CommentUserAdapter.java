package com.cinemates20.Utils.Adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CommentUserAdapter extends RecyclerView.Adapter<CommentUserAdapter.ViewHolder>{
    private Context context;
    private List<Comment> commentList;
    //interface for click item
    private CommentUserAdapter.ClickListener clickListener;

    public CommentUserAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorComment, txtComment, txtTimeAndDate;
        private ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            authorComment = v.findViewById(R.id.txtName);
            txtComment = v.findViewById(R.id.txtComment);
            txtTimeAndDate = v.findViewById(R.id.txtDateComment);
            imageView = v.findViewById(R.id.imageView5);
        }
    }

    @NonNull
    @Override
    public CommentUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        return new CommentUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentUserAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.authorComment.setText(comment.getAuthor());

        UserDAO userDAO = new UserDAO_Firestore(context.getApplicationContext());
        Uri uri = userDAO.getImageUri(comment.getAuthor());
        if(uri != null){
            Picasso.get().load(uri).transform(new CircleTransform()).into(holder.imageView);
        }

        holder.txtComment.setText(comment.getTextComment());
        //holder.txtTimeAndDate.setText(authorReview.getTimeAndDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setOnItemClickListener(CommentUserAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(List<Comment> authorList, int position, View view);
    }
}

