package com.example.checklist;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailID;
    private EditText passwordID;
    private Button login;
    private Button signUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    finish();
                    startActivity(new Intent(login.this, listview_layout.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.login_layout);

        mAuth.addAuthStateListener(mAuthListener);

        emailID = (EditText) findViewById(R.id.email_input);
        passwordID = (EditText) findViewById(R.id.password_input);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }

        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(login.this,register.class);
                startActivity(signUpIntent);
            }
        });
    }
    private void signIn(){
        String email = emailID.getText().toString().trim();
        String passwrd = passwordID.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(passwrd)){
            Toast.makeText(login.this, "Fields are Empty ", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(emailID.getText().toString().trim(),passwordID.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        finish();
                        startActivity(new Intent(login.this, listview_layout.class));
                        Toast.makeText(login.this, "Sign-in Problem", Toast.LENGTH_SHORT).show();
                    }
                 }
             });
        }
    }
}
