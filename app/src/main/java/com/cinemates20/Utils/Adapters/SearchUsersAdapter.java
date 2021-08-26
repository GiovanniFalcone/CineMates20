package com.cinemates20.Utils.Adapters;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>{

    private Context context;
    private List<User> searchedUserList;
    private List<String> receivedList, friends, sentList;
    //interface for click item
    private ClickListener clickListener;

    public SearchUsersAdapter(Context context, List<User> searchedUserList, List<String> sentList, List<String> receivedList, List<String> friends) {
        this.context = context;
        this.searchedUserList = searchedUserList;
        this.sentList = sentList;
        this.receivedList = receivedList;
        this.friends = friends;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, appendState;
        private Button button;
        private ImageView imageView;
        private String buttonState = "notFriend";
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            appendState = v.findViewById(R.id.txtAppendState);
            button = v.findViewById(R.id.button);
            imageView = v.findViewById(R.id.imageView6);

        }
    }

    @NonNull
    @Override
    public SearchUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        return new SearchUsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUsersAdapter.ViewHolder holder, int position) {
        User user = searchedUserList.get(position);

        holder.name.setText(user.getUsername());
        if(user.getIcon() != null){
            Picasso.get().load(Uri.parse(user.getIcon())).transform(new CircleTransform()).into(holder.imageView);
        }

        holder.button.setBackgroundColor(Color.BLUE);

        if(sentList.contains(user.getUsername())){
            holder.buttonState = "appendRequestSent";
            holder.button.setText("Cancella Richiesta");
            holder.button.setBackgroundColor(Color.RED);
        }else if (friends.contains(user.getUsername())){
            holder.buttonState = "friend";
            holder.button.setText("Rimuovi amico");
            holder.button.setBackgroundColor(Color.GREEN);
        }else if (receivedList.contains(user.getUsername())){
            holder.buttonState = "appendRequestReceived";
            holder.button.setText("Conferma amico");
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.gold));
            holder.appendState.setText("ha già inviato una richiesta");
        }else{
            holder.buttonState = "notFriend";
            holder.button.setText("Aggiungi Amico");
            holder.button.setBackgroundColor(Color.BLUE);
        }

        holder.button.setOnClickListener(view -> {
            switch (holder.buttonState) {
                case "notFriend":
                    Toast.makeText(view.getContext(), "Richiesta inviata a " + user.getUsername(),
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "appendRequestSent";
                    holder.button.setText("Cancella Richiesta");
                    holder.button.setBackgroundColor(Color.RED);
                    break;
                case "appendRequestSent":
                    Toast.makeText(view.getContext(), "Richiesta a " + user.getUsername() + " cancellata",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "notFriend";
                    holder.button.setText("Aggiungi Amico");
                    holder.button.setBackgroundColor(Color.BLUE);
                    break;
                case "friend":
                    Toast.makeText(view.getContext(), "Amicizia con " + user.getUsername() + " cancellata",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "notFriend";
                    holder.button.setText("Aggiungi Amico");
                    holder.button.setBackgroundColor(Color.BLUE);
                    break;
                case "appendRequestReceived":
                    Toast.makeText(view.getContext(), "Ora tu e " + user.getUsername() + " siete amici",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(user.getUsername(), holder.buttonState, position, view);
                    holder.buttonState = "friend";
                    holder.button.setText("Rimuovi amico");
                    holder.appendState.setText("");
                    holder.button.setBackgroundColor(Color.GREEN);
                    break;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(searchedUserList!=null)
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