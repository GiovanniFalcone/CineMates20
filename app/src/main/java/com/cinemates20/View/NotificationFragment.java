package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.Model.Notification;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.Presenter.NotificationPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.NotificationAdapter;

import java.util.List;


public class NotificationFragment extends Fragment {

    private NotificationPresenter notificationPresenter;
    private RecyclerView recyclerView;
    private FriendsRequestPresenter friendsRequestPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notificationRv);
        friendsRequestPresenter = new FriendsRequestPresenter(this);
        notificationPresenter = new NotificationPresenter(this);
        notificationPresenter.notificationClicked();
        return view;
    }

    public void setRecycler(List<Notification> notificationList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter notificationAdapter;
        notificationAdapter = new NotificationAdapter(getContext(), notificationList);
        recyclerView.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
        clickListner(notificationAdapter);
    }

    public void clickListner(NotificationAdapter notificationAdapter) {
        notificationAdapter.setOnItemClickListener((userWhoSentRequest, buttonType) -> {
            friendsRequestPresenter.manageAcceptOrDeclineFriendRequest(userWhoSentRequest, buttonType, getContext());
        });
    }

}