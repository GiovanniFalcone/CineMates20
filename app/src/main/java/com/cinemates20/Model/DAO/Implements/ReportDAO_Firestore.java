package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Firestore.ReportDAO;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportDAO_Firestore implements ReportDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reportsCollection = db.collection("reports");


    @Override
    public void addReport(String idReported, String author, String reporter, String typeReport, String typeReported) {
        // Add a new document with a generated ID
        DocumentReference documentReference = reportsCollection.document();

        Map<String, Object> report = new HashMap<>();
        report.put("idReport", documentReference.getId());
        report.put("idReported", idReported);
        report.put("author", author);
        report.put("reporter", reporter);
        report.put("typeReport", typeReport);
        report.put("typeReported", typeReported);
        report.put("dateAndTime", new Timestamp(new Date()));

        // Save data into document
        documentReference.set(report)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }
}
