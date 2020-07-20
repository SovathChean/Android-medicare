package com.example.mankind;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth auth;
    private EditText editEmail, editPassword;
    private Button login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        editPassword = findViewById(R.id.pass_edt_text);
        editEmail = findViewById(R.id.email_edt_text);
        login = findViewById(R.id.login_btn);

        login.setOnClickListener(this);


    }

        public void loginUser(){
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

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //display some message here
                        Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, setupActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        //display some message here
                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    @Override
    public void onClick(View v) {
        loginUser();
    }
}
