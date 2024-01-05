package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.snitcher.Model.UserModel;
import com.example.snitcher.databinding.ActivityMainBinding;
import com.example.snitcher.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LogInAcitvity.class));
            }
        });

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageuri="https://firebasestorage.googleapis.com/v0/b/snitcher-f3457.appspot.com/o/man.png?alt=media&token=993dfa86-4c53-47a4-9784-a145cc9827eb";
                String email = binding.uemail.getText().toString();
                String username = binding.uname.getText().toString();
                String password = binding.upassword.getText().toString();
                String lastMessage="";
                String status="";
                String bio="Hey There I Am Using Snitcher";
                String dob="";

                if (!email.isEmpty() || !username.isEmpty() || !password.isEmpty())
                {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String uid=task.getResult().getUser().getUid();
                                UserModel user = new UserModel(imageuri,email,username,password,uid,lastMessage,status,bio,dob);
                                database.getReference().child("user").child(uid).setValue(user);
                                Toast.makeText(getApplicationContext(),"Account Successfully Created!!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter All Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}