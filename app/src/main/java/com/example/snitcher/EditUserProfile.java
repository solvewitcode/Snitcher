package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.snitcher.databinding.ActivityEditUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditUserProfile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ActivityEditUserProfileBinding binding;
    Uri imageUri;
    String  imageuri;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityEditUserProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        reference=database.getReference("user").child(auth.getCurrentUser().getUid());
        String prevuserName=getIntent().getStringExtra("userName");
//        String prevEmail=getIntent().getStringExtra("Email");
//        String prevPassword=getIntent().getStringExtra("password");
        binding.EditProfileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=binding.EditProfileName.getText().toString();
                String bio=binding.EditProfileBio.getText().toString();
                String dob=binding.EditProfileDOB.getText().toString();
                if (imageUri!=null)
                {
                    StorageReference storageReference = storage.getReference().child("upload").child(auth.getCurrentUser().getUid());
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageuri=uri.toString();
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("profilepic", imageuri);
                                        reference.updateChildren(map);
                                    }
                                });
                            }
                        }
                    });
                }
                Toast.makeText(getApplicationContext(),username+" "+ bio
                        +" "+ dob,Toast.LENGTH_SHORT).show();
                if (!username.isEmpty()){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userName", username);
                    map.put("bio", bio);
                    map.put("dob", dob);
                    reference.updateChildren(map);
                }
//                else
//                {
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("userName", prevuserName);
//                    map.put("bio", bio);
//                    map.put("dob", dob);
//                    reference.updateChildren(map);
//                }
            }
        });
        binding.EditUserEditImageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EditUserProfile.this);
                dialog.setContentView(R.layout.upload_image_dialogue_box);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();
                Button gallery=dialog.findViewById(R.id.chooseImageGallery);
                Button camera=dialog.findViewById(R.id.chooseImageCamera);
                Button done=dialog.findViewById(R.id.DoneUploadImage);
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
                    }
                });
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Camera",Toast.LENGTH_SHORT).show();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10)
        {
            if (data!=null)
            {
                imageUri = data.getData();
                binding.EditProfileImage.setImageURI(imageUri);
            }
        }
    }
}