package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cinemates20.Model.Notification;
import com.cinemates20.Presenter.FriendsRequestPresenter;
import com.cinemates20.Presenter.NotificationPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;

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

        friendsRequestPresenter = new FriendsRequestPresenter(this, getContext());
        notificationPresenter = new NotificationPresenter(this);

        notificationPresenter.notificationClicked();

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    public void setRecycler(List<Notification> notificationList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GenericAdapter<Notification> personAdapter = new GenericAdapter<>(notificationList, getContext());
        recyclerView.setAdapter(personAdapter);
        clickListener(personAdapter, notificationList);
    }

    public void clickListener(GenericAdapter<Notification> genericAdapter, List<Notification> notificationList) {
        genericAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String userWhoSentRequest, String buttonType, int position) {
                friendsRequestPresenter.manageAcceptOrDeclineFriendRequest(userWhoSentRequest, buttonType);

                if(buttonType.equals("confirm"))
                    Toast.makeText(requireContext(),"Request accepted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(),"Request refused", Toast.LENGTH_SHORT).show();

                notificationList.remove(position);
                recyclerView.removeViewAt(position);
                genericAdapter.notifyItemRemoved(position);
                genericAdapter.notifyItemRangeChanged(position, genericAdapter.getItemCount());
            }
        });
    }

    @Override
    public void onRefresh() {
        notificationPresenter.notificationClicked();
        swipeRefreshLayout.setRefreshing(false);
    }
}