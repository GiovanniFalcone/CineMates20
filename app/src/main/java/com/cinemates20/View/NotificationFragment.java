package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cinemates20.Model.Notification;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.Presenter.NotificationPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.NotificationAdapter;

import java.util.List;


public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private NotificationPresenter notificationPresenter;
    private RecyclerView recyclerView;
    private FriendsRequestPresenter friendsRequestPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        recyclerView = view.findViewById(R.id.notificationRv);

        friendsRequestPresenter = new FriendsRequestPresenter(this);
        notificationPresenter = new NotificationPresenter(this);

        notificationPresenter.notificationClicked();

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    public void setRecycler(List<Notification> notificationList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NotificationAdapter notificationAdapter;
        notificationAdapter = new NotificationAdapter(getContext(), notificationList);
        recyclerView.setAdapter(notificationAdapter);
        clickListener(notificationAdapter, notificationList);
    }

    public void clickListener(NotificationAdapter notificationAdapter, List<Notification> notificationList) {
        notificationAdapter.setOnItemClickListener((userWhoSentRequest, buttonType, position) -> {
            friendsRequestPresenter.manageAcceptOrDeclineFriendRequest(userWhoSentRequest, buttonType, getContext());

            if(buttonType.equals("confirm"))
                Toast.makeText(requireContext(),"Richiesta accettata", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(requireContext(),"Richiesta rifiutata", Toast.LENGTH_SHORT).show();

            //Remove item whenever user accept or refuse request
            notificationList.remove(position);
            recyclerView.removeViewAt(position);
            notificationAdapter.notifyItemRemoved(position);
            notificationAdapter.notifyItemRangeChanged(position, notificationAdapter.getItemCount());
        });
    }

    @Override
    public void onRefresh() {
        notificationPresenter.notificationClicked();
        swipeRefreshLayout.setRefreshing(false);
    }
}