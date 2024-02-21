package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.snitcher.Adapters.MessagesAdapter;
import com.example.snitcher.Model.MessageModel;
import com.example.snitcher.databinding.ActivityChattingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingActivity extends AppCompatActivity {
    ActivityChattingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    CircleImageView imageView;
    public  static String senderImg;
    public  static String recieverIImg;
    String senderRoom,recieverRoom;
    ArrayList<MessageModel> messageArrayList;
    MessagesAdapter messagesAdapter;
    String senderId;
    Uri uri;
    String recieverId,recieverName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChattingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        messageArrayList= new ArrayList<>();
        messagesAdapter= new MessagesAdapter(ChattingActivity.this,messageArrayList);
        binding.chatRecyclerView.setAdapter(messagesAdapter);

        recieverId=getIntent().getStringExtra("userId");
        String recieverimage=getIntent().getStringExtra("profilePic");
        recieverName=getIntent().getStringExtra("userName");

        if (auth.getCurrentUser().getUid()!=null) {
            senderId = auth.getCurrentUser().getUid();
        }

//        System.out.println(senderId);
//        System.out.println(recieverId);
        senderRoom=senderId+recieverId;
        recieverRoom=recieverId+senderId;


        binding.chattingScreenRecieverName.setText(recieverName);
        imageView=findViewById(R.id.chattingScreenProfilePic);
        Picasso.get().load(recieverimage).into(imageView);

        DatabaseReference reference=database.getReference().child("user").child(senderId);
        DatabaseReference chatsreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatsreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageArrayList.add(messageModel);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg=snapshot.child("profilepic").getValue().toString();
                recieverIImg=recieverimage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message =binding.writeMessage.getText().toString();
                if (message.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Message",Toast.LENGTH_SHORT).show();
                }
                binding.writeMessage.setText("");
                Date date = new Date();
                MessageModel messageModel=new MessageModel(message,senderId,date.getTime(),"text");
                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages")
                        .push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(recieverRoom).child("messages")
                                        .push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
       binding.chatCameraIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Dialog dialog = new Dialog(ChattingActivity.this);
               dialog.setContentView(R.layout.upload_image_dialogue_box);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
               dialog.show();
               Button gallery=dialog.findViewById(R.id.chooseImageGallery);
               Button camera=dialog.findViewById(R.id.chooseImageCamera);
               Button done=dialog.findViewById(R.id.DoneUploadImage);
               gallery.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Toast.makeText(getApplicationContext(),"Gallery",Toast.LENGTH_SHORT).show();
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
                uri = data.getData();
                String url= null;
                if (uri != null) {
                    url = uri.toString();
                }
                Intent intent1=new Intent(ChattingActivity.this, SendImage.class);
                intent1.putExtra("u",url);
                intent1.putExtra("n",recieverName);
                intent1.putExtra("ruid",recieverId);
                intent1.putExtra("suid",senderId);
                startActivity(intent1);
            }
        }
    }
}