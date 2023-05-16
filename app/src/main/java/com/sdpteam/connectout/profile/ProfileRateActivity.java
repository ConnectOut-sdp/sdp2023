package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.ReportProfileActivity.REPORTED_UID;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

public class ProfileRateActivity extends AppCompatActivity {

    public final static String RATED_UID = "uid";
    public final static String RATED_NAME = "name";
    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_rate);
        // get the Id of the user
        String uid = getIntent().getStringExtra(RATED_UID);
        String name = getIntent().getStringExtra(RATED_NAME);
        // initiate rating bar and a button
        final RatingBar simpleRatingBar = findViewById(R.id.simpleRatingBar);
        Button submitButton = findViewById(R.id.submitRatingButton);
        // perform click event on button
        setListenerForRating(submitButton, simpleRatingBar, uid);

        Button reportButton = findViewById(R.id.reportUser);
        reportButton.setOnClickListener(v -> goToReportProfile(uid));
        TextView userText = findViewById(R.id.rateUserTextView);
        userText.setText("Rate " + name);
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
        intent.putExtra(REPORTED_UID, id);
        startActivity(intent);
        finish();
    }
}