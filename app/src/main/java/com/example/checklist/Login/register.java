package com.example.checklist.Login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.R;
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

public class register extends AppCompatActivity {
    private EditText register_email;
    private EditText register_password;

    private Button register;

    private User user;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUser;


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.register_layout);

        user = new User();
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        register_email = (EditText) findViewById(R.id.register_email_input);
        register_password = (EditText) findViewById(R.id.register_password_input);

        /* Listens when the register button gets clicked */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_user();
            }
        });
    }
    /**
     * Check if email is in the database
     */
    private boolean isValidEmail(String email, DataSnapshot dataSnapshot){
        User user = new User();

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            user.setEmail(ds.getValue(User.class).getEmail());

            if(user.getEmail().equalsIgnoreCase(email))
                return true;
        }
        return false;
    }
    /**
     * Register a new email and password to Firebase Authenticator
     */
    private void register_user(){
        final String email = register_email.getText().toString().trim();
        final String passwd = register_password.getText().toString();

        /* Listens for a value event */
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(isValidEmail(email, dataSnapshot))
                    Toast.makeText(register.this, "User Already Exists", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /* Checks if any of the fields are empty */
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(passwd)){
            Toast.makeText(this, "One or both fields were left empty", Toast.LENGTH_SHORT).show();
        }else{
            /* creates users accounts */
            mAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user.setUser_id(mAuth.getUid());
                        user.setEmail(email);
                        dbUser = FirebaseDatabase.getInstance().getReference();
                        dbUser.child("Users").child(mAuth.getUid()).setValue(user);
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(register.this, login.class));
                    }
//                    else{
//                        Toast.makeText(register.this, "Register Failed", Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        }




    }


}
