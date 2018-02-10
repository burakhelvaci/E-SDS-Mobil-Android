package com.esds.app.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.esds.app.R;

public class IncomingMessageActivity extends AppCompatActivity {

    TextView messageTitleTextView;
    TextView messageBodyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_message);

        messageTitleTextView = findViewById(R.id.message_title_in_incoming_message);
        messageBodyTextView = findViewById(R.id.message_body_in_incoming_message);

        String sender = getIntent().getExtras().getString("sender");
        String message = getIntent().getExtras().getString("message");

        messageTitleTextView.setText(sender);
        messageBodyTextView.setText(message);
    }
}
