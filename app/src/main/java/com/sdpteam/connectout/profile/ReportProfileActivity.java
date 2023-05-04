package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReportProfileActivity extends AppCompatActivity {

    public final static String REPORTED_UID = "reportUid";

    private final ReportProfileViewModel rvm = new ReportProfileViewModel(new ReportFirebaseDataSource());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_profile);
        String reportUid = getIntent().getStringExtra(REPORTED_UID);

        Button submitReportButton = findViewById(R.id.submitReportButton);
        submitReportButton.setOnClickListener(v -> sendReport(reportUid));
    }

    private void sendReport(String reportedUid) {
        EditText text = findViewById(R.id.ReportText);

        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String reporterUid = (au == null) ? NULL_USER : au.uid;
        rvm.saveReport(text.getText().toString(), reportedUid, reporterUid);
        Toast.makeText(getApplicationContext(), "Report submitted", Toast.LENGTH_LONG).show();
        finish();
    }
}