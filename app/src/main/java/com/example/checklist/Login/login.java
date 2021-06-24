package com.example.checklist.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.R;
import com.example.checklist.listview_layout;
import com.example.checklist.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {
    /* Variables */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference dbUser;
    private EditText emailID;
    private EditText passwordID;
    private Button login;
    private Button signUp;
    private TextView emailError;
    private TextView passwordError;
    private TextView userNotFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* FirebaseAuth instance */
        mAuth = FirebaseAuth.getInstance();

        /* Listens to for the AuthState */
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
        emailError = findViewById(R.id.emptyEmail);
        passwordError = findViewById(R.id.emptyPassword);
        userNotFound = findViewById(R.id.userNotFound);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                //userNotFound.setVisibility(View.VISIBLE);
            }

        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(login.this, register.class);
                startActivity(signUpIntent);
            }
        });
    }
    private boolean isRegister(String email, DataSnapshot dataSnapshot){
        User user = new User();
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            user.setEmail(ds.getValue(User.class).getEmail());
            if(user.getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }
    private void signIn(){
        final String email = emailID.getText().toString().trim();
        String passwrd = passwordID.getText().toString();
        boolean flag;
        if(TextUtils.isEmpty(email)){
            emailError.setVisibility(View.VISIBLE);
            Toast.makeText(login.this, "Email field is Empty ", Toast.LENGTH_SHORT).show();
        }else{

            if(TextUtils.isEmpty(passwrd)){
                emailError.setVisibility(View.INVISIBLE);
                passwordError.setVisibility(View.VISIBLE);
            }
            else {
                FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!isRegister(email, dataSnapshot)) {
                            userNotFound.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mAuth.signInWithEmailAndPassword(emailID.getText().toString().trim(),passwordID.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(login.this, listview_layout.class));
                        }
                    }
                });
            }
        }
    }

    public interface onCallBack{
        void onCallBack(boolean isReg);
    }
}
