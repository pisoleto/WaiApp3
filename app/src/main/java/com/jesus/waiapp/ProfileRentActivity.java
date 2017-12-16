package com.jesus.waiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileRentActivity extends AppCompatActivity {

    private TextView mNameProfileRent;
    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private Button mChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_rent);

        mNameProfileRent = (TextView)findViewById(R.id.nombreUsuarioProfileRent);
        mChatButton = (Button) findViewById(R.id.ChatButton);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mPost_key = getIntent().getExtras().getString("user_id");

        String post_key = getIntent().getExtras().getString("user_id");


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_descripcion =  dataSnapshot.child("nombreUsuario").getValue().toString();
                mNameProfileRent.setText(post_descripcion);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String post_descripcion =  dataSnapshot.child("nombreUsuario").getValue().toString();
                        mNameProfileRent.setText(post_descripcion);

                       // mPost_key = getIntent().getExtras().getString("blog_id");

                      //  final String post_key = getIntent().getExtras().getString("blog_id");

                        String uid_user =  dataSnapshot.child("uid").getValue().toString();


                        Intent ChatIntent = new Intent(ProfileRentActivity.this, ChatActivity.class);
                        ChatIntent.putExtra("uid_user", uid_user);
                        ChatIntent.putExtra("post_descripcion",  post_descripcion );
                        startActivity(ChatIntent);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });




















            }
        });

    }
}
