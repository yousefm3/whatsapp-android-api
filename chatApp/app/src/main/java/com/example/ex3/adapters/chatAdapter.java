package com.example.ex3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ex3.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ex3.Message;

import java.util.ArrayList;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter {

    Context context;
    List<Message> messagesArrayList;

    int ITEM_SEND = 1;
    int ITEM_RECIEVE = 2;

    public chatAdapter(Context context, List<Message> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.senderchatlayout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recieverchatlayout, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textViewmessaage.setText(messages.getContent());
            viewHolder.timeofmessage.setText(messages.getCreated().substring(messages.getCreated().length()-8,messages.getCreated().length() - 1));
        } else {
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.textViewmessaage.setText(messages.getContent());
            viewHolder.timeofmessage.setText(messages.getCreated());
        }


    }


    @Override
    public int getItemViewType(int position) {
        Message message = messagesArrayList.get(position);
        if (message.sent) {
              return ITEM_SEND;
       } else {
           return ITEM_RECIEVE;
       }
    }

    @Override
    public int getItemCount() {
        if(messagesArrayList != null) {
            return messagesArrayList.size();
        }
        return 0;
    }


    class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewmessaage;
        TextView timeofmessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage = itemView.findViewById(R.id.sendermessage);
            timeofmessage = itemView.findViewById(R.id.timeofmessage);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView textViewmessaage;
        TextView timeofmessage;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage = itemView.findViewById(R.id.sendermessage);
            timeofmessage = itemView.findViewById(R.id.timeofmessage);
        }
    }
    public void setMessagesArrayList(List<Message> s) {
        messagesArrayList = s;
        notifyDataSetChanged();
    }


}