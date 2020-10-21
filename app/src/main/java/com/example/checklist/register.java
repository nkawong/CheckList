package com.example.checklist;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private EditText register_email;
    private EditText register_password;

    private Button register;


    private FirebaseAuth mAuth;
    private DatabaseReference dbUser;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.register_layout);

        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        register_email = (EditText) findViewById(R.id.register_email_input);
        register_password = (EditText) findViewById(R.id.register_password_input);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_user();
            }
        });
    }
    private void register_user(){
        String email = register_email.getText().toString().trim();
        String passwd = register_password.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(passwd)){
            Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        dbUser = FirebaseDatabase.getInstance().getReference("Users");
                        dbUser.child(mAuth.getUid()).setValue(mAuth.getCurrentUser());
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(register.this, login.class));
                    }else{
                        Toast.makeText(register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }
}
