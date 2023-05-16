package com.sdpteam.connectout.QrCode;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

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
    public boolean handleScanResult(String resultText) {
        boolean handled = false;
        if (resultText != null) {
            String[] parts = resultText.split("/");
            if (parts.length == 2) {
                String type = parts[0];
                String id = parts[1];
                if (type.equals("event")) {
                    EventActivity.openEvent(QRcodeActivity.this, id);
                    handled = true;
                } else if (type.equals("profile")) {
                    ProfileActivity.openProfile(QRcodeActivity.this, id);
                    handled = true;
                }
            }
        }
        finish();
        return handled;
    }

}