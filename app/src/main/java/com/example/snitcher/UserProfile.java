package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.snitcher.Model.UserModel;
import com.example.snitcher.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ActivityUserProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        reference=database.getReference("user").child(auth.getCurrentUser().getUid());
        final UserModel[] user1 = new UserModel[1];
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                binding.userProfileName.setText(user.getUserName());
                binding.userProfileEmail.setText(user.getMail());
                user1[0] =user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.userProfileEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this,EditUserProfile.class);
                intent.putExtra("userName",user1[0].getUserName());
                intent.putExtra("Email",user1[0].getMail());
                intent.putExtra("password",user1[0].getPassword());
                startActivity(intent);
            }
        });
    }
}