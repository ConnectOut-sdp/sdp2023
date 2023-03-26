package com.sdpteam.connectout.qr_code;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sdpteam.connectout.R;

public class QRcodeEventActivity extends AppCompatActivity {

    private Button btn_show_qr_code;

    private final String PROFILE_BASE_URI = "https://connect-out.com/events/";

    private String EVENT_ID = "0123456789"; // we will need to fetch it from local database (to display our own profile)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_event);

        btn_show_qr_code = findViewById(R.id.show_qr_code_btn);
        btn_show_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrCodeData = PROFILE_BASE_URI + EVENT_ID;
                Intent intent = new Intent(QRcodeEventActivity.this, QRcodeModalActivity.class);
                intent.putExtra("title", "Profile QR code");
                intent.putExtra("qrCodeData", qrCodeData);
                qrCodeLauncher.launch(intent);
            }
        });

    }

    private ActivityResultLauncher<Intent> qrCodeLauncher = registerForActivityResult(
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