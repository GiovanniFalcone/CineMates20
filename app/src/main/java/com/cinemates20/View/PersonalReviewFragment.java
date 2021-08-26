package com.cinemates20.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinemates20.Presenter.PersonalReviewPresenter;
import com.cinemates20.R;


public class PersonalReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button seeNumberReactions;
    private TextView personalReview, reviewTitleMovie;
    private EditText comment;
    private ImageView buttonBack;
    private PersonalReviewPresenter personalReviewPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_review, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        seeNumberReactions = view.findViewById(R.id.seeAllReaction);
        reviewTitleMovie = view.findViewById(R.id.textView11);
        personalReview = view.findViewById(R.id.textReviewAuthor);
        comment = view.findViewById(R.id.editTextScriviCommento);
        buttonBack = view.findViewById(R.id.imageViewBackButton);

        reviewTitleMovie.setText(requireArguments().getString("movieTitle"));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        personalReviewPresenter = new PersonalReviewPresenter(this);
        personalReviewPresenter.viewPersonalReview();

        seeNumberReactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personalReviewPresenter.onClickNumberReactions();
            }
        });

        return view;
    }

    public String getMovieTitle(){
        return requireArguments().getString("movieTitle");
    }

    public void setReview(String review){
        personalReview.setText(review);
    }



}