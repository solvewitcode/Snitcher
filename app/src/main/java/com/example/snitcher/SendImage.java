package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.snitcher.Model.MessageModel;
import com.example.snitcher.databinding.ActivitySendImageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class SendImage extends AppCompatActivity {

    String url,recieverName,senderId,recieverId;
    Uri imageUrl;
    FirebaseStorage firebaseStorage;
    MessageModel messageModel;
    Uri uri;
    String senderRoom,recieverRoom;
    ActivitySendImageBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySendImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messageModel=new MessageModel();
        firebaseStorage=FirebaseStorage.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            url=bundle.getString("u");
            recieverName=bundle.getString("n");
            senderId=bundle.getString("suid");
            recieverId=bundle.getString("ruid");
        }

        senderRoom=senderId+recieverId;
        recieverRoom=recieverId+senderId;
        System.out.println(senderRoom);
        System.out.println(recieverRoom);
        Picasso.get().load(url).into(binding.sendImageImageView);
        imageUrl=Uri.parse(url);

        binding.sendImageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUrl!=null)
                {
                    StorageReference storageReference =firebaseStorage.getReference().child("Message Images").
                            child(senderId);
                    storageReference.putFile(imageUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUri=uri;
                                        Date date=new Date();
                                        messageModel.setMessage(downloadUri.toString());
                                        messageModel.setSenderId(senderId);
                                        messageModel.setTimeStamp(date.getTime());
                                        messageModel.setType("image");
                                        database=FirebaseDatabase.getInstance();
                                        database.getReference().child("chats").child(senderRoom).child("messages")
                                                .push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        database.getReference().child("chats").child(recieverRoom).child("messages")
                                                                .push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}