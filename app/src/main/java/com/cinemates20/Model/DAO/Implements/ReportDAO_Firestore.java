package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReportDAO;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReportDAO_Firestore implements ReportDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reportsCollection = db.collection("reports");

    @Override
    public void addReport(String idReported, String author, String reporter, String typeReported) {
        // Add a new document with a generated ID
        DocumentReference documentReference = reportsCollection.document();
        Map<String, Object> report = new HashMap<>();
        report.put("idReport", documentReference.getId());
        report.put("idReported", idReported);
        report.put("author", author);
        report.put("reporters", Collections.singletonList(reporter));
        report.put("typeReported", typeReported);
        report.put("dateAndTime", new Timestamp(new Date()));
        report.put("reportCounter", 1);
        // Save data into document
        documentReference.set(report)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void updateReport(String idReported, String reporter) {
        // Update the report with the new reporter
        reportsCollection
                .whereEqualTo("idReported", idReported)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String idUserDocument = documentSnapshot.getId();
                            // Add the new reporter to the report document
                            reportsCollection
                                    .document(idUserDocument)
                                    .update("reporters", FieldValue.arrayUnion(reporter));
                            // increment the reportCounter
                            reportsCollection
                                    .document(idUserDocument)
                                    .update("reportCounter", FieldValue.increment(1));
                        }
                    }
                });
    }

    @Override
    public boolean checkIfReportExist(String idReported) {
        boolean result = false;
        // Create a query against the collection.
        Query query = reportsCollection.whereEqualTo("idReported", idReported);
        //Get query results
        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);
        if(task.isSuccessful())
            result = !Objects.requireNonNull(task.getResult()).isEmpty();
        return result;
    }
}
