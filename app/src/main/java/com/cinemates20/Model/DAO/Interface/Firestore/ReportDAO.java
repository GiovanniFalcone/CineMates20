package com.cinemates20.Model.DAO.Interface.Firestore;

public interface ReportDAO {
    void addReport(String idReported, String author, String reporter, String typeReport, String typeReported);
}
