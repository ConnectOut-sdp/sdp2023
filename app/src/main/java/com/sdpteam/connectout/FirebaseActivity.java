package com.sdpteam.connectout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

public class FirebaseActivity extends AppCompatActivity {

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
    }

    public void get(View view) {
        final CompletableFuture<String> future = new CompletableFuture<>();

        db.child(getPhone()).get().addOnSuccessListener(data -> {
            if (data.exists()) {
                future.complete(data.getValue(String.class));
            } else {
                future.completeExceptionally(new NoSuchFieldException());
            }
        }).addOnFailureListener(future::completeExceptionally);

        future.thenAccept(email -> {
            final TextView field = findViewById(R.id.email);
            field.setText(email);
        });
    }

    public void set(View view) {
        db.child(getPhone()).setValue(getEmail());
    }

    private String getEmail() {
        final TextView field = findViewById(R.id.email);
        return field.getText().toString();
    }

    private String getPhone() {
        final TextView field = findViewById(R.id.phone);
        return field.getText().toString();
    }
}