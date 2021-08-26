package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.Notification;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    //interface for click item
    private ClickListener clickListener;

    //private String buttonState = "notFriend";
    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, time = null;
        private final Button buttonConfirm, buttonDelete;
        private final ImageView icon;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.txtName);
            //time = v.findViewById(R.id.time);
            buttonConfirm = v.findViewById(R.id.buttonAccetta);
            buttonDelete = v.findViewById(R.id.buttonRifiuta);
            icon = v.findViewById(R.id.imageView8);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        private final TextView textNotification;
        private final ImageView icon;

        public ViewHolder2(View v) {
            super(v);
            textNotification = v.findViewById(R.id.txtNotification);
            icon = v.findViewById(R.id.imageView9);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(notificationList.get(position).getType().equals("RequestAccepted"))
            return 1;
        else if(notificationList.get(position).getType().equals("RequestReceived"))
            return 0;
        else return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=null;
        if(viewType == 0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_friend_request, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
            return new ViewHolder(v);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_notification_row, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
            return new ViewHolder2(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        UserDAO userDAO = new UserDAO_Firestore(context.getApplicationContext());

        String userWhoSentRequest = notificationList.get(position).getUserWhoSent();
        Uri uri = userDAO.getImageUri(userWhoSentRequest);

        if(itemType == 0){
            ((ViewHolder)holder).title.setText(userWhoSentRequest);
            Picasso.get().load(uri).transform(new CircleTransform()).into(((ViewHolder) holder).icon);
            ((ViewHolder)holder).buttonConfirm.setOnClickListener(view -> clickListener.onItemClickListener(userWhoSentRequest, "confirm"));
            ((ViewHolder)holder).buttonDelete.setOnClickListener(view -> clickListener.onItemClickListener(userWhoSentRequest, "delete"));
        }else if(itemType == 1){
            ((ViewHolder2)holder).textNotification.setText(userWhoSentRequest + " ha accettato la tua richiesta");
            Picasso.get().load(uri).transform(new CircleTransform()).into(((ViewHolder2) holder).icon);
        }
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(String userWhoSentRequest, String buttonType);
    }

}
