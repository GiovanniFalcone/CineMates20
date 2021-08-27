package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private Context context;
    private List<String> friends;
    //interface for click item
    private ClickListener clickListener;

    public FriendAdapter(Context context, List<String> friends) {
        this.context = context;
        this.friends = friends;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final Button button;
        private String buttonState = "friend";
        private final ImageView icon;

        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            button = v.findViewById(R.id.button);
            icon = v.findViewById(R.id.imageView6);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String user = friends.get(position);

        UserDAO userDAO = new UserDAO_Firestore(context.getApplicationContext());
        Uri uri = userDAO.getImageUri(user);

        holder.name.setText(user);
        holder.button.setText("Rimuovi amico");
        holder.button.setBackgroundColor(Color.GREEN);

        if(!uri.toString().equals(""))
            Picasso.get().load(uri).transform(new CircleTransform()).into(holder.icon);

        holder.button.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Amicizia con " + user + " cancellata",
                    Toast.LENGTH_SHORT).show();
            clickListener.onItemClickListener(user, holder.buttonState, position, view);
            holder.buttonState = "notFriend";
            holder.button.setText("Aggiungi Amico");
            holder.button.setBackgroundColor(Color.BLUE);
        });
    }

    @Override
    public int getItemCount() {
            return friends.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(String user, String buttonState, int position, View view);
    }
}