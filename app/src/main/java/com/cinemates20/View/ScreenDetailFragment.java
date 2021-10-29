package com.cinemates20.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cinemates20.R;

import info.movito.themoviedbapi.model.Artwork;

public class ScreenDetailFragment extends Fragment {

    public ScreenDetailFragment() {
        // Required empty public constructor
    }

    public static ScreenDetailFragment newInstance(Artwork artwork, String transitionName) {
        ScreenDetailFragment screenDetailFragment = new ScreenDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("artwork_item", artwork);
        bundle.putString("transition_name", transitionName);
        screenDetailFragment.setArguments(bundle);
        return screenDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Artwork artwork = (Artwork) requireArguments().getSerializable("artwork_item");
        String transitionName = requireArguments().getString("transition_name");

        ImageView imageView = view.findViewById(R.id.imageViewClicked);
        imageView.setTransitionName(transitionName);

        Glide.with(this)
                .load("https://www.themoviedb.org/t/p/original" + artwork.getFilePath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}