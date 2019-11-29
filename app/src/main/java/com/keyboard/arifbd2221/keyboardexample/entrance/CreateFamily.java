package com.keyboard.arifbd2221.keyboardexample.entrance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keyboard.arifbd2221.keyboardexample.R;
import com.keyboard.arifbd2221.keyboardexample.model.User;

public class CreateFamily extends AppCompatActivity {

    private EditText inputEmail, inputPassword, fullname, education, phone;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private String userId;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference profileReference;

    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.createfamily);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        profileReference = firebaseDatabase.getReference("Profile");

        editor = getSharedPreferences("username",MODE_PRIVATE).edit();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        fullname = findViewById(R.id.fullname);
        education = findViewById(R.id.education);
        phone = findViewById(R.id.phone);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateFamily.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateFamily.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(CreateFamily.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                userId = auth.getCurrentUser().getUid();
                                Log.e("before child","setting up child node");

                                User user = new User(fullname.getText().toString(), education.getText().toString(), phone.getText().toString()
                                                    , email);

                                profileReference.child(userId).setValue(user);

                                Log.e("after child","setting up child node ,"+auth.getCurrentUser().getUid());
                                if (!task.isSuccessful()) {
                                    Toast.makeText(CreateFamily.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(CreateFamily.this, EntranceActivity.class));
                                    finish();
                                }
                                Log.e("before starts","setting up child node");
                                editor.putString("name", fullname.getText().toString());
                                editor.putString("userid",auth.getCurrentUser().getUid());
                                editor.apply();
                                startActivity(new Intent(CreateFamily.this, EntranceActivity.class));
                                Log.e("after starts","setting up child node");
                                finish();
                            }
                        });

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
