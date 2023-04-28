package com.sdpteam.connectout.qr_code;

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

    private Button btnScan;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(v -> scanCode());

        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            handleScanResult(result.getContents());
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volum up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }

    private void handleScanResult(String resultText) {
        if (resultText != null) {
            Intent intent = new Intent(QRcodeActivity.this, EventActivity.class);
            intent.putExtra("url", resultText);
            startActivity(intent);
        }
    }
}