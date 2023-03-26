package com.sdpteam.connectout.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

public class ProfileRateActivity extends AppCompatActivity {

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_rate);
        // get the Id of the user
        String uid = getIntent().getStringExtra("uid");
        Profile userProfile = pvm.getProfile(uid).getValue();
        // initiate rating bar and a button
        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        Button submitButton = (Button) findViewById(R.id.submitRatingButton);
        // perform click event on button
        setListenerForRating(submitButton, simpleRatingBar, uid);

        Button reportButton = (Button) findViewById(R.id.reportUser);
        reportButton.setOnClickListener(v -> goToReportProfile(uid));
    }

    private void setListenerForRating(Button submit, RatingBar ratingBar, String uid) {
        submit.setOnClickListener(v -> {
            // get values and then displayed in a toast
            String totalStars = "Thanks for Rating ";
            double ratingValue = ratingBar.getRating();
            String rating = "Rating: " + ratingValue;
            Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();

            // update rating
            pvm.updateRating(uid, ratingValue);
            finish();
        });
    }

    private void goToReportProfile(String id) {
        Intent intent = new Intent(ProfileRateActivity.this, ReportProfileActivity.class);
        intent.putExtra("reportUid", id);
        startActivity(intent);
        finish();
    }
}