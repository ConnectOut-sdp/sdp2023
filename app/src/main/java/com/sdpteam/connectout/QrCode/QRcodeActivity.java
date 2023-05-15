package com.sdpteam.connectout.QrCode;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class QRcodeActivity extends AppCompatActivity {

    private ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            handleScanResult(result.getContents());
        });

        scanCode();
    }

    /**
     * launches the scanner of QR codes.
     */
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volum up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivityContainer.class);

        barLauncher.launch(options);
    }

    /**
     * TODO should finish this activity
     * TODO should identify correctly if we have an Event or a Profile
     * TODO should use the methods openProfile or openEvent.
     * Launches the corresponding activity from the QR code
     *
     * @param resultText (String): id of module
     */
    private void handleScanResult(String resultText) {
        if (resultText != null) {
            // parsing
            String[] parts = resultText.split("/");
            if (parts.length == 2) {
                String type = parts[0]; // "event" or "profile"
                String id = parts[1]; // the ID value

                if (type.equals("event")) {
                    // Navigate to event using the ID
                    EventActivity.openEvent(getApplicationContext(), id);
                } else if (type.equals("profile")) {
                    // Navigate to profile using the ID
                    ProfileFragment.setupFragment(id);
                } else {
                    // Invalid type -> we go back
                    finish();
                }
            } else {
                // Invalid URL format -> we go back
                finish();
            }
        }
    }

}