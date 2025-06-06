package com.example.kobarifinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText txtEmail;
    EditText txtPassword;
    Button btnLogin;
    Button btnLogout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Have I met you app");

        auth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);

        //login access app
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    authenticate(email, password);
                }if(email.isEmpty()){
                    txtEmail.setError("Please enter your email");
                }if(password.isEmpty()){
                    txtPassword.setError("Please enter your password");
                }
            }
        });
    }

    //method to go to RaceListActivity if email and password is authenticated
    private void authenticate(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Authenticated");
                    Intent i = new Intent(LoginActivity.this, RaceListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Failed to login");
                }
            }
        });
    }
}