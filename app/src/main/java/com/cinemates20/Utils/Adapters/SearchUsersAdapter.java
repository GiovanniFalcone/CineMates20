package com.cinemates20.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.R;

import java.util.List;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>{

    private final Context context;
    private final List<User> searchedUserList;
    private final List<String> receivedList;
    private final List<String> friends;
    private final List<String> sentList;
    private final String username; //username of currentUser
    //interface for click item
    private ClickListener clickListener;

    public SearchUsersAdapter(Context context, List<User> searchedUserList, List<String> sentList, List<String> receivedList, List<String> friends, String username) {
        this.context = context;
        this.searchedUserList = searchedUserList;
        this.sentList = sentList;
        this.receivedList = receivedList;
        this.friends = friends;
        this.username = username;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final Button button;
        private final ImageView imageView;
        private String buttonState = "notFriend";

        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            button = v.findViewById(R.id.button);
            imageView = v.findViewById(R.id.imageView6);
        }
    }

    @NonNull
    @Override
    public SearchUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new SearchUsersAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchUsersAdapter.ViewHolder holder, int position) {
        User user = searchedUserList.get(position);
        holder.name.setText(user.getUsername());

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();

        if(user.getIcon() != null){
            if(!user.getIcon().equals("")){
                Glide.with(context.getApplicationContext())
                    .load(user.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CircleCrop())
                    .into(holder.imageView);
            }
        }else if(!userDAO.getImageUri(user.getUsername()).toString().equals("")){
            Glide.with(context.getApplicationContext())
                    .load(userDAO.getImageUri(user.getUsername()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CircleCrop())
                    .into(holder.imageView);
        }

        if(sentList.contains(user.getUsername())){
            holder.buttonState = "appendRequestSent";
            holder.button.setText("Request send");
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
        }else if (friends.contains(user.getUsername())){
            holder.buttonState = "friend";
            holder.button.setText("Remove");
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
        }else if (receivedList.contains(user.getUsername())){
            holder.buttonState = "appendRequestReceived";
            holder.button.setText("Confirm");
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
        }else{
            holder.buttonState = "notFriend";
            holder.button.setText("Add");
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));

            if(username.equals(user.getUsername()))
                holder.button.setVisibility(View.INVISIBLE);
        }

        holder.button.setOnClickListener(view -> {
            switch (holder.buttonState) {
                case "notFriend":
                    Toast.makeText(view.getContext(), "Request send to " + user.getUsername(),
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "appendRequestSent";
                    holder.button.setText("Request send");
                    holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
                    break;
                case "appendRequestSent":
                    Toast.makeText(view.getContext(), "Request to " + user.getUsername() + " canceled",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "notFriend";
                    holder.button.setText("Add");
                    holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                    break;
                case "friend":
                    Toast.makeText(view.getContext(), "Friendship with " + user.getUsername() + " canceled",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "notFriend";
                    holder.button.setText("Add");
                    holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
                    break;
                case "appendRequestReceived":
                    Toast.makeText(view.getContext(), "Now you and " + user.getUsername() + " are friends",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "friend";
                    holder.button.setText("Remove");
                    holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
                    break;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(searchedUserList != null)
            return searchedUserList.size();
        else
            return friends.size();
    }

    public void setOnItemClickListener(SearchUsersAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    
    public interface ClickListener {
        void onItemClickListener(String user, String buttonState, int position, View view);
    }

}