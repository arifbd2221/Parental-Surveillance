package com.keyboard.arifbd2221.keyboardexample.joinfamily;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keyboard.arifbd2221.keyboardexample.R;
import com.keyboard.arifbd2221.keyboardexample.model.Child;

public class JoinFamily extends AppCompatActivity {

    private EditText code;
    private Button join;

    public String codeText;
    public String name;


    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference childs;

    public SharedPreferences.Editor editor;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_family);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Families");
        childs = firebaseDatabase.getReference("Families");

        editor = getSharedPreferences("Parentkey", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("username", MODE_PRIVATE);

        name = sharedPreferences.getString("name", null);


        code = findViewById(R.id.code);
        join = findViewById(R.id.join);




        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinToFamily();
            }
        });

    }


    private void joinToFamily(){
        codeText = code.getText().toString();
        Log.e("Code: ", codeText);
        databaseReference.orderByChild("family_code").equalTo(codeText).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Log.e("Key", userSnapshot.getKey());

                    Child child = new Child(auth.getCurrentUser().getUid(),name);
                    SharedPreferences.Editor editorusername = getSharedPreferences("username", MODE_PRIVATE).edit();
                    editorusername.putString("family", userSnapshot.getKey());
                    editorusername.apply();
                    childs.child(userSnapshot.getKey()).child("Childs").push().setValue(child);
                        editor.putString("key", userSnapshot.getKey());
                        editor.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}
