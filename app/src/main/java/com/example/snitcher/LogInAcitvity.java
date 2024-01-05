package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.snitcher.databinding.ActivityLogInAcitvityBinding;
import com.example.snitcher.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogInAcitvity extends AppCompatActivity {
    ActivityLogInAcitvityBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogInAcitvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.DontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInAcitvity.this,SignUpActivity.class));
            }
        });
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.uemail.getText().toString();
                String password = binding.upassword.getText().toString();
                if (!email.isEmpty() || !password.isEmpty())
                {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Signed In Successfully!!",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LogInAcitvity.this,MainActivity.class));
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
        if (auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LogInAcitvity.this,MainActivity.class));
        }
    }
}