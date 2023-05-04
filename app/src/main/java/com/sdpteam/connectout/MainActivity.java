package com.sdpteam.connectout;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.QrCode.QRcodeActivity;
import com.sdpteam.connectout.QrCode.QRcodeEventActivity;
import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
import com.sdpteam.connectout.QrCode.QRcodeProfileActivity;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.nearbyEvents.EventsActivity;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.MyEventsCalendarActivity;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileRateActivity;
import com.sdpteam.connectout.profile.ReportProfileActivity;
import com.sdpteam.connectout.profileList.ProfileListActivity;
import com.sdpteam.connectout.registration.CompleteRegistrationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();

        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), EventsActivity.class);
        this.startActivity(drawerIntent);

        findViewById(R.id.launcher_button_google_login).setOnClickListener(v -> start(GoogleLoginActivity.class, false));
        findViewById(R.id.launcher_button_complete_registration).setOnClickListener(v -> start(CompleteRegistrationActivity.class, false));
        findViewById(R.id.launcher_button_other_profile).setOnClickListener(v -> start(ProfileActivity.class, false));
        findViewById(R.id.launcher_button_my_profile).setOnClickListener(v -> start(ProfileActivity.class, true));
        findViewById(R.id.launcher_button_profile_edit).setOnClickListener(v -> start(EditProfileActivity.class, false));
        findViewById(R.id.launcher_button_profile_rate).setOnClickListener(v -> start(ProfileRateActivity.class, false));
        findViewById(R.id.launcher_button_profile_report).setOnClickListener(v -> start(ReportProfileActivity.class, false));
        findViewById(R.id.launcher_button_profile_list).setOnClickListener(v -> start(ProfileListActivity.class, false));
        findViewById(R.id.launcher_button_all_events).setOnClickListener(v -> start(EventsActivity.class, false));
        findViewById(R.id.launcher_button_event).setOnClickListener(v -> start(EventActivity.class, false));
        findViewById(R.id.launcher_button_event_creator).setOnClickListener(v -> start(EventCreatorActivity.class, false));
        findViewById(R.id.launcher_button_chat).setOnClickListener(v -> start(ChatActivity.class, false));
        findViewById(R.id.launcher_button_qrcode).setOnClickListener(v -> start(QRcodeActivity.class, false));
        findViewById(R.id.launcher_button_qrcode_event).setOnClickListener(v -> start(QRcodeEventActivity.class, false));
        findViewById(R.id.launcher_button_qrcode_modal).setOnClickListener(v -> start(QRcodeModalActivity.class, false));
        findViewById(R.id.launcher_button_qrcode_profile).setOnClickListener(v -> start(QRcodeProfileActivity.class, false));
        findViewById(R.id.launcher_button_registered_events_calendar).setOnClickListener(v -> start(MyEventsCalendarActivity.class, false));


    }

    private void start(Class c, boolean nullUid) {
        String uid = "KrbXharVGMeEdI8mEB7vBX74aSX2";
        if (nullUid){
            uid = null;
        }
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra("uid", uid);
        intent.putExtra("reportUid", "KrbXharVGMeEdI8mEB7vBX74aSX2");
        intent.putExtra(PASSED_ID_KEY, "NTUSaNxgOjqUyJTqXg6");
        this.startActivity(intent);
    }

    private void enablingFirebaseCache() {
        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}