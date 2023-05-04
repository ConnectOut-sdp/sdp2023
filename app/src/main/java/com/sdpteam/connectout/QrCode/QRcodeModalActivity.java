package com.sdpteam.connectout.QrCode;

import com.sdpteam.connectout.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that shows the generated QR code.
 */
public class QRcodeModalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_modal);

        ImageView qrCodeImageView = findViewById(R.id.qrcode_image);
        TextView modalTitle = findViewById(R.id.modal_title);

        // retrieving data from intent
        String title = getIntent().getStringExtra("title");
        String qrCodeData = getIntent().getStringExtra("qrCodeData");

        // displaying the title and the qr code
        modalTitle.setText(title);
        Bitmap qrCodeBitmap;
        try {
            qrCodeBitmap = QRcodeGenerator.generateQRCode(qrCodeData);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());
    }
}