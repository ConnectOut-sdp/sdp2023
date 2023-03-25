package com.sdpteam.connectout.qr_code;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCodeScanner {
    private final Activity activity;
    private final ScanOptions options;

    public QRCodeScanner(Activity activity, ScanOptions options) {
        this.activity = activity;
        this.options = options;
    }

    public void scan() {
        Intent intent = new Intent(activity, CaptureActivity.class);
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "Scan a qr code");

        activity.startActivityForResult(intent, 0);
    }

    public void handleResult(Intent data) {
        String contents = data.getStringExtra(Intents.Scan.RESULT);
        if (contents != null) {
            // Here, you can add the logic to handle the scanned QR code.
            // For example, you could launch a new activity or open a URL.
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents));
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Scan failed", Toast.LENGTH_SHORT).show();
        }
    }
}

