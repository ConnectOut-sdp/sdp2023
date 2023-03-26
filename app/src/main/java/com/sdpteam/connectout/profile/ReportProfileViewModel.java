package com.sdpteam.connectout.profile;

import androidx.lifecycle.ViewModel;

public class ReportProfileViewModel extends ViewModel {

    private ReportModel reportModel;

    public ReportProfileViewModel(ReportModel reportModel) {
        this.reportModel = reportModel;
    }

    /**
     * Save a report
     */
    public void saveReport(String report, String reportedUid, String reporterUid) {
        reportModel.saveReport(report, reportedUid, reporterUid);
    }
}
