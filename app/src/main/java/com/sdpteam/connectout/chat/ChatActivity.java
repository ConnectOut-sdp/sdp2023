package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

public class ChatActivity extends AppCompatActivity {
    ChatViewModel viewModel = new ChatViewModel(new ChatModel());
    AuthenticatedUser au = new GoogleAuth().loggedUser();

    String uid = (au == null) ? NULL_USER : au.uid;

    private FirebaseListAdapter<ChatMessage> adapter;


    private final String userName = viewModel.getProfileUserName();

    String chatId;

    public static final String NULL_CHAT = "null_chat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chatId");
        chatId = (chatId == null)? NULL_CHAT : chatId;

        /*viewModel.saveMessage(new ChatMessage("Luc", "Luc", "Il est trop fort Aymeric", chatId));
        viewModel.saveMessage(new ChatMessage("Mikael", "Mikael", "Vraiment", chatId));
        viewModel.saveMessage(new ChatMessage("Eric", "Eric", "C'est fou comme je t admire mec", chatId));*/

        //we enable the return button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText input = findViewById(R.id.chat_input);
        FloatingActionButton chatFab = findViewById(R.id.chat_fab);

        chatFab.setOnClickListener((view) ->{
            viewModel.saveMessage(new ChatMessage(userName, uid, input.getText().toString(), chatId));
            input.setText("");
        });

        setUpListAdapter();
    }

    /**
     * Returns to the previous activity if the return button in the Actionbar is pressed
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO swap with an if statement
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpListAdapter() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        //TODO there shouldn't be any Firebase in the view
        //passer populateView au model dans une lambda
        //so a solution is probably to send populate View as a lambda to a function in the Model
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(ChatModel.chatsPathString)
                .child(chatId)
                .limitToLast(50);

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message)//Note: The guide doesn't mention this method, without it an exception is thrown that the layout has to be set.
                .setQuery(query, ChatMessage.class)
                .setLifecycleOwner(this)   //Added this
                .build();
        /*adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()){*/
        adapter = new FirebaseListAdapter<ChatMessage>(options){
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage chatMessage, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set the text of the message's view
                messageText.setText(chatMessage.getMessageText());
                messageUser.setText(chatMessage.getUserName());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        chatMessage.getMessageTime()));

                //your own messages should be to the right of the screen
                if (chatMessage.getUserId().equals(uid)){
                    messageText.setGravity(Gravity.RIGHT);
                    messageUser.setText("You");
                    messageUser.setGravity(Gravity.CENTER);
                    messageTime.setGravity(Gravity.RIGHT);
                    Drawable backgroundDrawable = DrawableCompat.wrap(v.getBackground()).mutate();
                    //DrawableCompat.setTint(backgroundDrawable, 0xFFF);
                    backgroundDrawable.setColorFilter( 0x58F8F800, PorterDuff.Mode.ADD );
                }
                else{
                    messageTime.setGravity(Gravity.RIGHT);
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}