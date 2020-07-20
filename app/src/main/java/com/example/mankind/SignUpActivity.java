package com.example.mankind;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
   private FirebaseAuth auth;
   private EditText editEmail;
   private EditText editPassword;
   private Button signUp;
   private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.email_edt_text);
        editPassword = findViewById(R.id.pass_edt_text);
        signUp = findViewById(R.id.signup_btn);
        login = findViewById(R.id.login_btn);

        signUp.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void registerUser(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //display some message here
                    Toast.makeText(SignUpActivity.this, "Succesfully Register", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, setupActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //display some message here
                    Toast.makeText(SignUpActivity.this, "Register Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}
