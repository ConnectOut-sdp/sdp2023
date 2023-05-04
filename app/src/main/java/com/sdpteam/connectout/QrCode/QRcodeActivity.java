package com.sdpteam.connectout.QrCode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.viewer.EventActivity;

public class QRcodeActivity extends AppCompatActivity {

    private ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Button btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(v -> scanCode());

        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            handleScanResult(result.getContents());
        });
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
     * <p>
     * Launches the corresponding activity from the QR code
     *
     * @param resultText (String): id of module
     */
    private void handleScanResult(String resultText) {
        if (resultText != null) {
            Intent intent = new Intent(QRcodeActivity.this, EventActivity.class);
            intent.putExtra("url", resultText);
            startActivity(intent);
        }
    }
}