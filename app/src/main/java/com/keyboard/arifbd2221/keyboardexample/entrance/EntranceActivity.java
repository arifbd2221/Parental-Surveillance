package com.keyboard.arifbd2221.keyboardexample.entrance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keyboard.arifbd2221.keyboardexample.R;
import com.keyboard.arifbd2221.keyboardexample.family.MyFamily;
import com.keyboard.arifbd2221.keyboardexample.joinfamily.JoinFamily;
import com.keyboard.arifbd2221.keyboardexample.login.LoginActivity;

public class EntranceActivity extends AppCompatActivity {


    private Button createFamily, joinFamily;

    public SharedPreferences.Editor editor;

    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public FirebaseAuth auth;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Families");

        int familyCode = (int)(Math.random() * 50 + 1);
        databaseReference.child(auth.getCurrentUser().getUid()).child("family_code").setValue(familyCode+"");

        editor = getSharedPreferences("UserState", MODE_PRIVATE).edit();


        init();

    }

    private void init() {
        createFamily = findViewById(R.id.create_family);
        joinFamily = findViewById(R.id.join_family);

        createFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editor.putString("user", "parent");
                    editor.apply();
                    startActivity(new Intent(EntranceActivity.this, MyFamily.class));
            }
        });

        joinFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editor.putString("user", "child");
                    editor.apply();
                    startActivity(new Intent(EntranceActivity.this, JoinFamily.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.login:
                startActivity(new Intent(EntranceActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
