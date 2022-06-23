package com.example.ex3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ex3.Contact;
import com.example.ex3.ContactWithMessages;
import com.example.ex3.Message;
import com.example.ex3.R;
import com.example.ex3.RecyclerViewItem;
import com.example.ex3.contactsList;
import com.example.ex3.loginActivity;


import java.util.List;

public class contactsListAdapter extends RecyclerView.Adapter<contactsListAdapter.contactViewHolder> {
    public static List<Contact> contacts;
    private final LayoutInflater mInflater;
    private RecyclerViewItem recyclerViewItem;
    private contactsList act;
    int row_index = -1;

    public contactsListAdapter(Context context, RecyclerViewItem recyclerViewItem) {
        mInflater = LayoutInflater.from(context);
        act = (contactsList)context;
        this.recyclerViewItem = recyclerViewItem;
    }

    class contactViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView contactImage;
        private final TextView created;
        private final TextView lastMessage;
        private final RelativeLayout layout;

        private contactViewHolder(View itemView, RecyclerViewItem recyclerViewItem) {
            super(itemView);
            name = itemView.findViewById(R.id.nameofuser);
            contactImage = itemView.findViewById(R.id.imageviewofuser);
//            layout = itemView.findViewById(R.id.layout);
            created = itemView.findViewById(R.id.lastMessageDate);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            layout = itemView.findViewById(R.id.specific_contact);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewItem != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewItem.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }


    @Override
    public contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.chatviewlayout, parent, false);
        return new contactViewHolder(itemView, recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(contactViewHolder holder, int position) {
        if (contacts != null) {
            Uri myUri = null;
            Contact current = contacts.get(position);
            if(current.getImageID()!= null) {
                myUri = Uri.parse(current.getImageID());
            }


            holder.contactImage.setImageURI(myUri);
            holder.name.setText(current.getContactDisplayName());
            holder.layout.setOnClickListener(v -> {
                act.goChat(current);
            });
            ContactWithMessages q = loginActivity.userDao.getMessages(current.getContactUsername());
            if (q != null && q.messages.size() > 0) {
                holder.created.setText(q.messages.get(q.messages.size() - 1).created);
                if (q.messages.get(q.messages.size() - 1).content.length() < 10)
                    holder.lastMessage.setText(q.messages.get(q.messages.size() - 1).content);
                else {
                    holder.lastMessage.setText(q.messages.get(q.messages.size() - 1).content.substring(0,10) + "...");
                }
            }
        }
//        int x = position;
//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                row_index=x;
//                notifyDataSetChanged();
//            }
//        });
//        if (row_index == position){
//            holder.layout.setBackgroundColor(Color.GRAY);
//        } else{
//            holder.layout.setBackgroundColor(Color.parseColor("#dddddd"));
//        }

    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> s) {
        contacts = s;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        }
        return 0;
    }
}