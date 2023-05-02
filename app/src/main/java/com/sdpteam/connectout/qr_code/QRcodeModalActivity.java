package com.sdpteam.connectout.qr_code;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

public class QRcodeModalActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView modalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_modal);

        qrCodeImageView = findViewById(R.id.qrcode_image);
        modalTitle = findViewById(R.id.modal_title);

        // retrieving data from intent
        String title = getIntent().getStringExtra("title");
        String qrCodeData = getIntent().getStringExtra("qrCodeData");

        // displaying the title and the qr code
        modalTitle.setText(title);
        QRcodeGenerator qrCode = new QRcodeGenerator();
        Bitmap qrCodeBitmap = null;
        try {
            qrCodeBitmap = qrCode.generateQRCode(qrCodeData);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());
    }
}