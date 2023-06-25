package com.example.quizandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_From extends AppCompatActivity {

    EditText txt_fullName,txt_username,txt_email,txt_password;
    Button btn_Signup;
    RadioGroup myRadioGroup;
    RadioButton radio_admin,radio_user;

    DatabaseReference databaseReference;
    String accout ="";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Dialog load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_from);

        txt_fullName = (EditText) findViewById(R.id.txt_fullname);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_Signup = (Button) findViewById(R.id.btn_Signup);
        radio_admin = (RadioButton) findViewById(R.id.radio_admin);
        radio_user = (RadioButton) findViewById(R.id.radio_user);
        myRadioGroup =(RadioGroup) findViewById(R.id.my_radio_group);

        databaseReference = FirebaseDatabase.getInstance().getReference("Member");
        firebaseAuth = FirebaseAuth.getInstance();

        load = new Dialog(this);
        load.setContentView(R.layout.load);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.ic_action_bookmaark));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = txt_fullName.getText().toString();
                String username = txt_username.getText().toString();
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();

                if (radio_admin.isChecked()){
                    accout ="admin";
                }if (radio_user.isChecked()){
                    accout="user";
                }

                if(txt_email.getText().toString().isEmpty()||txt_fullName.getText().toString().isEmpty()||txt_username.getText().toString().isEmpty()||txt_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();

                }
                else if (myRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn một tùy chọn trong RadioGroup", Toast.LENGTH_SHORT).show();
                }
                else {
                    load.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(Signup_From.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        member information = new member(
                                                fullName,
                                                username,
                                                email,
                                                password,
                                                accout

                                        );

                                        FirebaseDatabase.getInstance().getReference("Member")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(Signup_From.this, "Create a successful account", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(),Login_From.class));
                                                        load.dismiss();
                                                        finish();

                                                    }
                                                });

                                    }else {
                                        Toast.makeText(Signup_From.this, "Kiểm tra lại thông tin đã nhập !", Toast.LENGTH_SHORT).show();
                                        load.dismiss();
                                        finish();
                                    }
                                }
                            });

                }



            }
        });

    }
}