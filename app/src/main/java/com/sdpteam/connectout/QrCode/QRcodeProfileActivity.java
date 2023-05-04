package com.sdpteam.connectout.QrCode;

import com.sdpteam.connectout.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// TODO this class is to be removed soon - add a button in the profile page instead
public class QRcodeProfileActivity extends AppCompatActivity {

    private final String PROFILE_BASE_URI = "https://connect-out.com/profiles/";
    private final String PROFILE_ID = "0123456789"; // we will need to fetch it from local database (to display our own profile)
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
    private Button btn_show_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_profile);

        btn_show_qr_code = findViewById(R.id.show_qr_code_btn);
        btn_show_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrCodeData = PROFILE_BASE_URI + PROFILE_ID;
                Intent intent = new Intent(QRcodeProfileActivity.this, QRcodeModalActivity.class);
                intent.putExtra("title", "Profile QR code");
                intent.putExtra("qrCodeData", qrCodeData);
                qrCodeLauncher.launch(intent);
            }
        });
    }
}