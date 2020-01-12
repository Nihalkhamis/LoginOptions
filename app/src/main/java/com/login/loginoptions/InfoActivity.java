package com.login.loginoptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        profile_pic = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        Intent intent = getIntent();
        String fisrt_name = intent.getStringExtra("First_name");
        String last_name = intent.getStringExtra("Last_name");
        String email = intent.getStringExtra("Email");
        String id = intent.getStringExtra("ID");
        String image_url = intent.getStringExtra("Image_url");

    }
}
