package com.cinemates20.Utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utils {

    public static void showErrorDialog(Context context, String title, String msg){
        new MaterialAlertDialogBuilder(context)
                .setIcon(R.drawable.ic_baseline_error_24)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", /* listener = */ null)
                .show();
    }

    public static void showDialog(Context context, String title, String msg){
        new MaterialAlertDialogBuilder(context)
                .setIcon(R.drawable.ic_baseline_check_24)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", /* listener = */ null)
                .show();
    }


    public static boolean checkIfFieldIsEmpty(String...strings){
        for(String s: strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }

    public static void putImage(Context context, String url, ImageView poster){
        Glide.with(context.getApplicationContext())
                .load("http://image.tmdb.org/t/p/original"+ url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 300)
                .transform(new RoundedCorners(30))
                .into(poster);
    }

    public static void changeFragment(Fragment currentFragment, Fragment newFragment, int idLayout){
        currentFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(idLayout, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public static Task<QuerySnapshot> waitTask(Task<QuerySnapshot> task){
        // Block on a task and get the result synchronously.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            try{
                Tasks.await(task);
            } catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        });
        //Waits for the computation to complete, and then retrieves its result.
        try {
            future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return task;
    }

}
