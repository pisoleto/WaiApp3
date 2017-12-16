package com.jesus.waiapp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class AddGameAddPhase extends AppCompatActivity {


    private String mPost_key = null;
    private EditText mValueField;

    private Button mSubmitBtn;

    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseNombreJuego;
    private DatabaseReference  mDatabaseUsuario;


/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_add_phase);

        mValueField = (EditText)findViewById(R.id.titleField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mPost_key = getIntent().getExtras().getString("blog_id");

        final String post_key = getIntent().getExtras().getString("blog_id");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Alquilados");

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = post_key.toString();
                String value = mValueField.getText().toString();

                DatabaseReference childRef = mDatabase.child(key);
                childRef.push().setValue(value);

            }
        });
    }*/

/* DEFINITIVOOOOOOOOOOOOOOOOOOOOO!!!!!!!!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_add_phase);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mValueField = (EditText)findViewById(R.id.titleField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mPost_key = getIntent().getExtras().getString("blog_id");

        final String post_key = getIntent().getExtras().getString("blog_id");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Alquilados");

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = post_key.toString();
                String value = mValueField.getText().toString();
                String usuario = mCurrentUser.getUid();

                DatabaseReference childRef = mDatabase.child(key);
                DatabaseReference hola= childRef.child(usuario);

                hola.child("precio").setValue(value);


            }
        });
    }


*/





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_add_phase);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseNombreJuego = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUsuario = FirebaseDatabase.getInstance().getReference().child("Users");


        mValueField = (EditText)findViewById(R.id.titleField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);



        mPost_key = getIntent().getExtras().getString("blog_id");




        final String post_key = getIntent().getExtras().getString("blog_id");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Alquilados");













        mDatabaseNombreJuego.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String nombreJuego = (String) dataSnapshot.child("title").getValue();

                mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String key = post_key.toString();

                        String value = mValueField.getText().toString();
                        String usuario = mCurrentUser.getUid();
                        String fotoPerfil = mCurrentUser.getPhotoUrl().toString();

                        String nombreUsuario = mCurrentUser.getDisplayName();

                        DatabaseReference childRef = mDatabase.child(key);
                        DatabaseReference hola= childRef.child(usuario);

                        hola.child("precioAlquiler").setValue(value);
                        hola.child("nombreUsuario").setValue(nombreUsuario);
                        hola.child("fotoPerfil").setValue(fotoPerfil);
                        hola.child("nombreJuego").setValue(nombreJuego);





                        DatabaseReference rifi= mDatabaseUsuario.child(usuario);
                        DatabaseReference paco = rifi.child("Games");
                        DatabaseReference eustaquio = paco.child(key);



                        eustaquio.child("nombreJuego").setValue(nombreJuego);
                        eustaquio.child("rol").setValue("alquiler");


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }







}
