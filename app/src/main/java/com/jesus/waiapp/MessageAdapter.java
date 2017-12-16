package com.jesus.waiapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.subinkrishna.widget.CircularImageView;

import java.util.List;

/**
 * Created by Jesus Manuel on 27/11/2017.
 */


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircularImageView profileImage;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircularImageView) view.findViewById(R.id.message_profile_layout);


        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();

        /*
        if (from_user.equals(current_user_id)){

            //si nuestro usuario id coincide con el nuestro el fondo del message single layout se pondra de un color y sino de otro

            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);


        }else {

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);

        }

        */

        viewHolder.messageText.setText(c.getMessage());


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



}