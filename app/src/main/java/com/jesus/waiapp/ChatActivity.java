package com.jesus.waiapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.subinkrishna.widget.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private String uid_user;

    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircularImageView mProfileImage;

    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private RecyclerView mMessagesList;

    private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;


    // NEW SOLUTION

    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // habria que hacer lo de checkear si el usuario esta conectado o no y si no te envien al login

        mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);


        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
//CAMBIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        //USER_ID POR BLOG_ID y el usern_name por post_descripcion

        // mChatUser = getIntent().getStringExtra("blog_id");


        //  mPost_key = getIntent().getExtras().getString("blog_id");

        // String post_key = getIntent().getStringExtra("blog_id");


        String userName = getIntent().getStringExtra("post_descripcion");
        final String uid_user = getIntent().getStringExtra("uid_user");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);


        mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircularImageView) findViewById(R.id.custom_bar_image);

        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList);

        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);

        mLinearLayout = new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        loadMessages();


        mTitleView.setText(userName);


        mRootRef.child("Users").child(uid_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                // String image = dataSnapshot.child("image").getValue().toString();

                if (online.equals("true")) {
                    mLastSeenView.setText("Online");
                } else {


                    // ESTO ES PARA PONER LA HORA EN QUE SE HA CONECTADO, LO HEMOS COGIDO DE UN SCRIPT
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(online);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());
                    mLastSeenView.setText(lastSeenTime);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(uid_user)) {

                    //si no hay creado el usuario que lo haga
                    // CON ESTO VAMOS A CREAR EL NODO DE CHAT, DENTRO DE ESTE CREA DOS NODOS DE PERSONAS A Y B.
                    // DE ESTA FORMA NODO A CONTIENE EL NODO B CON DATOS DE SEEN Y TIMESTAMP Y VICEVERSA. SOLO
                    // LOS VA A CREAR SI NO EXISTE LAS CONVERSACION


                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + uid_user, chatAddMap);
                    chatUserMap.put("Chat/" + uid_user + "/" + mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }


                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

    {
        @Override
        public void onRefresh () {

        mCurrentPage++;

        itemPos = 0;
        loadMoreMessages();

    }


    });

}


    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(mCurrentUserId).child(uid_user);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {


                    messagesList.add(itemPos++, message);


                } else {
                    mPrevKey = mLastKey;
                }


                if (itemPos == 1) {


                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key:" + mLastKey + " / Prev Key: " + mPrevKey + "/ Message Key: " + messageKey);

                mAdapter.notifyDataSetChanged();


                mRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPositionWithOffset(10, 0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadMessages() {

        final String uid_user = getIntent().getStringExtra("uid_user");

        DatabaseReference messageRef = mRootRef.child("messages").child(mCurrentUserId).child(uid_user);

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                itemPos++;
                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;


                }

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1); //esto nos mueve la lista hacia abajo

                mRefreshLayout.setRefreshing(false);

                // Swipe/Pull to Refresh for Android RecyclerView (or any other vertically scrolling view) BLOG BUENOOOOOOOOOOOOOO


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void sendMessage() {


        // ESTO CREA UN NODO PRINCIPAL QUE SE LLAMA MESSAGES CON DOS NODOS DE DOS PERSONAS A Y B
        //EL NODO A TIENE EL NODO B CON OTRO NODO ID DEL MENSAJE CON LOS DATOS DEL MENSAJES, SEEN, TIME, TYPE YYYYY VICEVERSA


        String message = mChatMessageView.getText().toString();
        if (!TextUtils.isEmpty(message)) {

            final String uid_user = getIntent().getStringExtra("uid_user");

            String current_user_ref = "messages/" + mCurrentUserId + "/" + uid_user;
            String chat_user_ref = "messages/" + uid_user + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("messages").child(mCurrentUserId).child(uid_user).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mChatMessageView.setText(""); // esto es para que cuando termine de escribir se borre el edittext

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }
                }
            });


        }


    }








































































       /* AL FINAL NO HACE ESTO. LO VA A MANDAR EN EL STRING EXTRA DE LA ANTERIOR ACTIVIDAD

        mRootRef.child("Users").child(uid_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //esto es para coger el nombre del usuario de un unico nodo. MUY UTIL

                String chat_user_name = dataSnapshot.child("name").getValue().toString();
                 getSupportActionBar().setTitle(uid_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        */


}
