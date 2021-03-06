package com.cinemates20.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateUtils;
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

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utils {

    public static void showErrorDialog(Context context, String title, String msg){
        new MaterialAlertDialogBuilder(context, R.style.ThemeMyAppDialogAlertDay)
                .setIcon(R.drawable.ic_baseline_error_24)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", /* listener = */ null)
                .show();
    }

    public static void showDialog(Context context, String title, String msg){
        new MaterialAlertDialogBuilder(context, R.style.ThemeMyAppDialogAlertDay)
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

    public static void changeFragment_BottomAnim(Fragment currentFragment, Fragment newFragment, int idLayout){
        currentFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.bottom_out  // popExit
                )
                .add(idLayout, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void changeFragment_SlideAnim(Fragment currentFragment, Fragment newFragment, int idLayout){
        currentFragment.getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(idLayout, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public static Task<?> waitTask(Task<?> task){
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

    public static String setTime(long time) {
        long now = Calendar.getInstance().getTime().getTime();
        long difference = now - time;

        if (difference < 60 * 1000) {
            return  "moments ago";
        } else if (difference < 2 * DateUtils.MINUTE_IN_MILLIS) {
            return "a minute ago";
        } else if (difference < 60 * DateUtils.MINUTE_IN_MILLIS) {
            return (difference / DateUtils.MINUTE_IN_MILLIS + " minutes ago");
        } else if (difference < 2 * DateUtils.HOUR_IN_MILLIS) {
            return ("an hour ago");
        } else if (difference < 24 * DateUtils.HOUR_IN_MILLIS) {
            return (difference / DateUtils.HOUR_IN_MILLIS + " hours ago");
        } else if (difference < 48 * DateUtils.HOUR_IN_MILLIS) {
            return ("yesterday");
        } else {
            return (difference / DateUtils.DAY_IN_MILLIS + " days ago");
        }
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
