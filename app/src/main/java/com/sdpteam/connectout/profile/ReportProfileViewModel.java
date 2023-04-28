package com.sdpteam.connectout.profile;

import androidx.lifecycle.ViewModel;

public class ReportProfileViewModel extends ViewModel {

    private final ReportFirebaseDataSource reportFirebaseDataSource;

    public ReportProfileViewModel(ReportFirebaseDataSource reportFirebaseDataSource) {
        this.reportFirebaseDataSource = reportFirebaseDataSource;
    }

    /**
     * Save a report
     */
    public void saveReport(String report, String reportedUid, String reporterUid) {
        reportFirebaseDataSource.saveReport(report, reportedUid, reporterUid);
    }
}
