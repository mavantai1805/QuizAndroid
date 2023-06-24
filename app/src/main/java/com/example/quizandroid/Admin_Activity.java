package com.example.quizandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Admin_Activity extends AppCompatActivity {
    private Button startBtn,btn_SignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(view -> {
            Intent categoryIntent = new Intent(Admin_Activity.this,CategoriesActivity_Admin.class);
            startActivity(categoryIntent);


        });



        btn_SignOut = findViewById(R.id.btn_Signout);
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent signout = new Intent(Admin_Activity.this,Login_From.class);
                startActivity(signout);
                finish();

            }
        });

    }


}