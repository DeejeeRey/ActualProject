package com.dgr790.wrkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity{

    private EditText emailET, passEt;
    private Button loginBtn, registerBtn;
    private String[] userInformation;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInformation = new String[7];

        emailET = (EditText) findViewById(R.id.emailET);
        passEt = (EditText) findViewById(R.id.passET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailET.getText().toString();
                final String pass = passEt.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    showMessage("Please fill out all fields.");
                } else {
                    signIn(email,pass);
                }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    private void goToRegister() {
        Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerActivity);
        finish();
    }

    // Sign in with firebase authentication
    private void signIn(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    updateUI();
                } else {
                    showMessage("Login Failed.");
                }

            }
        });

    }

    // Dummy info for main activity
    private void addDummyInfo() {
        userInformation[0] = "";
        userInformation[1] = "";
        userInformation[2] = "";
        userInformation[3] = "";
        userInformation[4] = "";
        userInformation[5] = "";
        userInformation[6] = "false";
    }

    @Override
    public void onBackPressed() {
    }

    private void updateUI() {
        addDummyInfo();
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        homeActivity.putExtra("userInformation", userInformation);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String m) {
        Toast.makeText(LoginActivity.this, m, Toast.LENGTH_SHORT).show();
    }

    // Used if user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI();
        }
    }
}
