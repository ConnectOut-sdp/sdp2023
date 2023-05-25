package com.sdpteam.connectout.QrCode;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
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
     * Launches the corresponding activity from the QR code
     *
     * @param resultText (String): id of module
     */
    public boolean handleScanResult(String resultText) {
        boolean handled = false;
        if (resultText != null) {
            String[] parts = resultText.split("/");
            if (parts.length == 2) {
                handled = handleQrCodeTypes(parts[0], parts[1]);
            }
        }
        finish();
        return handled;
    }

    private boolean handleQrCodeTypes(String type, String id) {
        if (type.equals("event")) {
            EventActivity.openEvent(QRcodeActivity.this, id);
            return true;
        }
        if (type.equals("profile")) {
            ProfileActivity.openProfile(QRcodeActivity.this, id);
            return true;
        }
        return false;
    }
}