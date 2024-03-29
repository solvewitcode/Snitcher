package com.example.snitcher.Adapters;

import static com.example.snitcher.ChattingActivity.recieverIImg;
import static com.example.snitcher.ChattingActivity.senderImg;

import android.content.Context;
import android.net.wifi.rtt.RangingResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snitcher.Model.MessageModel;
import com.example.snitcher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageModelArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MessagesAdapter(Context context, ArrayList<MessageModel> messageModelArrayList) {
        this.context = context;
        this.messageModelArrayList = messageModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout,parent,false);
            return new recieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel=messageModelArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class)
        {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            if (messageModel.getType().equals("text")) {
                viewHolder.msgtxt.setText(messageModel.getMessage());
            }
            if (messageModel.getType().equals("image"))
            {
                viewHolder.msgtxt.setVisibility(View.GONE);
                viewHolder.iv_sender.setVisibility(View.VISIBLE);
                Picasso.get().load(messageModel.getMessage()).into(viewHolder.iv_sender);
            }
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else
        {
            recieverViewHolder viewHolder = (recieverViewHolder) holder;
            if (messageModel.getType().equals("text")) {
                viewHolder.msgtxt.setText(messageModel.getMessage());
            }
            if (messageModel.getType().equals("image"))
            {
                viewHolder.msgtxt.setVisibility(View.GONE);
                viewHolder.iv_reciever.setVisibility(View.VISIBLE);
                Picasso.get().load(messageModel.getMessage()).into(viewHolder.iv_reciever);
            }
            Picasso.get().load(recieverIImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel= messageModelArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIEVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView iv_sender;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.profilerggg);
            msgtxt=itemView.findViewById(R.id.msgsendertyp);
            iv_sender=itemView.findViewById(R.id.iv_sender);
        }
    }
    class recieverViewHolder extends  RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView iv_reciever;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.pro);
            msgtxt=itemView.findViewById(R.id.recivertextset);
            iv_reciever=itemView.findViewById(R.id.iv_reciever);
        }
    }
}
