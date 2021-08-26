package com.cinemates20.Presenter;

import android.content.DialogInterface;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.cinemates20.View.ReviewCardActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportPresenter {

    private ReviewCardActivity reviewCardActivity;

    public ReportPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void onClickOption(){
        final CharSequence[] charSequence = new CharSequence[] {"Spoiler","Linguaggio inappropriato"};
        AtomicInteger selected = new AtomicInteger();

        new MaterialAlertDialogBuilder(reviewCardActivity.getActivityContext())
                .setTitle("Effettua segnalazione")
                .setSingleChoiceItems(charSequence, 0, (dialogInterface, i) -> selected.set(i))
                .setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (selected.get()){
                            case 0:
                                Toast.makeText(reviewCardActivity.getActivityContext(), "Segnalazione per spoiler inviata", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(reviewCardActivity.getActivityContext(), "Segnalazione per linguaggio inappropriato inviata", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> dialogInterface.dismiss())
        .show();
    }

}
