package com.cinemates20.Model.DAO.Interface.InterfaceDAO;

public interface ReportDAO {
    void addReport(String idReported, String author, String reporter, String typeReported);

    void updateReport(String idReported, String reporter);

    boolean checkIfReportExist(String idReported);
}
