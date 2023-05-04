package com.sdpteam.connectout.QrCode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

// TODO this class is to be removed soon - add a button in the event page instead
public class QRcodeEventActivity extends AppCompatActivity {

    private Button btnShowQrCode;

    private static final String PROFILE_BASE_URI = "https://connect-out.com/events/";

    private final String EVENT_ID = "0123456789"; // we will need to fetch it from local database (to display our own profile)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_event);

        btnShowQrCode = findViewById(R.id.show_qr_code_btn);
        btnShowQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrCodeData = PROFILE_BASE_URI + EVENT_ID;
                Intent intent = new Intent(QRcodeEventActivity.this, QRcodeModalActivity.class);
                intent.putExtra("title", "Event QR code");
                intent.putExtra("qrCodeData", qrCodeData);
                qrCodeLauncher.launch(intent);
            }
        });

    }

    private final ActivityResultLauncher<Intent> qrCodeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // Do something when the QRCodeActivity is finished
                    }
                }
            });
}