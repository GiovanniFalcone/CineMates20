package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cinemates20.Presenter.MyListsPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.MyListsAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class MyListsFragment extends Fragment {

    private MyListsPresenter myListsPresenter;
    private RecyclerView recyclerView;
    private Button buttonNewList;
    private TextInputLayout chooseNameListLayout;
    private EditText editTextChooseNameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lists, container, false);

        recyclerView = view.findViewById(R.id.myListRecyclerView);

        myListsPresenter = new MyListsPresenter(this);
        myListsPresenter.myListClicked();

        buttonNewList = view.findViewById(R.id.newList);
        buttonNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
                builder.setTitle("Creazione lista");
                builder.setMessage("Assegna un nome alla nuova lista:");
                View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_new_list, (ViewGroup) getView(), false);
                chooseNameListLayout = viewDialog.findViewById(R.id.chooseNameListLayout);
                editTextChooseNameList = viewDialog.findViewById(R.id.chooseNameList);
                builder.setView(viewDialog);
                builder.setPositiveButton("Si", (dialogInterface, i) -> {
                    myListsPresenter.onClickNewList(editTextChooseNameList.getText().toString().trim());
                })
                        .setNegativeButton("Annulla", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void setRecycler(List<String> listName) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MyListsAdapter myListsAdapter = new MyListsAdapter(getContext(), listName);
        recyclerView.setAdapter(myListsAdapter);
        clickListener(myListsAdapter);
    }

    public void clickListener(MyListsAdapter myListsAdapter){
        myListsAdapter.setOnItemClickListener(new MyListsAdapter.ClickListener() {
            @Override
            public void onItemClickListener(String listClicked) {
                myListsPresenter.onClickSeeList(listClicked);
            }
        });
    }
}