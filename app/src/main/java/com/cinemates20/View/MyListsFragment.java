package com.cinemates20.View;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cinemates20.Model.MovieList;
import com.cinemates20.Presenter.MyListsPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.cinemates20.Utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class MyListsFragment extends Fragment {

    private MyListsPresenter myListsPresenter;
    private RecyclerView recyclerView;
    private Button buttonNewList;
    private TextInputLayout chooseNameListLayout;
    private EditText editTextChooseNameList, description;
    private GenericAdapter<MovieList> myListsAdapter;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lists, container, false);

        recyclerView = view.findViewById(R.id.myListRecyclerView);

        myListsPresenter = new MyListsPresenter(this);
        myListsPresenter.myListClicked();

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Movie lists");
        toolbar.setNavigationOnClickListener(view13 -> requireActivity().onBackPressed());

        showDialog();

        buttonNewList = view.findViewById(R.id.newList);
        buttonNewList.setOnClickListener(view1 -> {
            description.addTextChangedListener(new EditTextListener());
            editTextChooseNameList.addTextChangedListener(new EditTextListener());

            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        });

        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeMyAppDialogAlertDay);
        builder.setTitle("List creation");
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_new_list, (ViewGroup) getView(), false);
        editTextChooseNameList = viewDialog.findViewById(R.id.chooseNameList);
        chooseNameListLayout = viewDialog.findViewById(R.id.chooseNameListLayout);
        description = viewDialog.findViewById(R.id.editTextBoxDescription);
        builder.setView(viewDialog);
        builder.setMessage("Give a name to new list:");
        builder.setPositiveButton("Create", (dialogInterface, i) -> {
            myListsPresenter.onClickNewList(editTextChooseNameList.getText().toString().trim(), description.getText().toString().trim());
            editTextChooseNameList.getText().clear();
            description.getText().clear();
        })
                .setNegativeButton("Back", (dialogInterface, i) -> {
                    editTextChooseNameList.getText().clear();
                    description.getText().clear();
                    dialogInterface.dismiss();
                });

        alertDialog = builder.create();
    }

    public void setRecycler(List<MovieList> movieList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        myListsAdapter = new GenericAdapter<>(movieList, getContext());
        recyclerView.setAdapter(myListsAdapter);
        clickListener(myListsAdapter);
    }

    public void clickListener(GenericAdapter<MovieList> myListsAdapter){
        myListsAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(MovieList listClicked) {
                myListsPresenter.onClickSeeList(listClicked);
            }
        });
    }

    public void updateRecycler() {
        myListsAdapter.notifyItemInserted(myListsAdapter.getItemCount() + 1);
    }

    private class EditTextListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String descriptionText = description.getText().toString().trim();
            String nameList = editTextChooseNameList.getText().toString().trim();

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!Utils.checkIfFieldIsEmpty(nameList, descriptionText));
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}