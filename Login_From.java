package com.example.sad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Login_From extends AppCompatActivity {

    private EditText txt_email,txt_password,txt_reset;
    private Button login;

    private Dialog load;

    public void btn_signupForm(View view){
        startActivity(new Intent(getApplicationContext(),Signup_From.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_from);

        txt_email = findViewById(R.id.txt_email_LG);
        txt_password = findViewById(R.id.txt_password_LG);
        login = findViewById(R.id.btn_Login);

        load = new Dialog(this);
        load.setContentView(R.layout.load);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bookmark_border));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                if(txt_email.getText().toString().isEmpty()||txt_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập thông tin email & password!", Toast.LENGTH_SHORT).show();
                }
                else {
                         Login(email, password);
                            load.show();
                }
            }
        });

    }
    public void btn_resetPassword(View view){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = txt_email.getText().toString();
        if(txt_email.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập thông tin email!", Toast.LENGTH_SHORT).show();

        }
        else {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login_From.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login_From.this, "Email Sent Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private  void Login(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser member = firebaseAuth.getCurrentUser();
                            checkUserRole(member.getUid());
                        }
                        else{
                            Toast.makeText(Login_From.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            load.dismiss();
                            finish();
                        }
                    }
                });

    }

    private void  checkUserRole(String uid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Member").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String role = dataSnapshot.child("Accout").getValue(String.class);
                    if (role.equals("user")){
                        Intent intent = new Intent(Login_From.this,MainActivity.class);
                        startActivity(intent);
                        load.dismiss();
                        finish();
                    }else {
                        Intent intent = new Intent(Login_From.this,Admin_Activity.class);
                        startActivity(intent);
                        load.dismiss();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login_From.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                load.dismiss();
                finish();
            }
        });

    }




}