package com.jesus.waiapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameDescription extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private  TextView mBlogSingleDesc;
    private TextView mBlogLanzamiento;
    private TextView mBlogGenero;
    private TextView mBlogDescripcion;
    private ImageButton mAlquilarBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_description);







        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBlogDescripcion = (TextView) findViewById(R.id.descripcionJuego);
        mBlogSingleDesc = (TextView) findViewById(R.id.singleBlogDesc);
        mBlogSingleTitle = (TextView) findViewById(R.id.singleBlogTitle);
        mBlogLanzamiento = (TextView) findViewById(R.id.lanzamiento);
        mAlquilarBtn = (ImageButton) findViewById(R.id.alquilarBtn);

        mBlogGenero = (TextView) findViewById(R.id.genero);

        mBlogSingleImage = (ImageView) findViewById(R.id.singleBlogImage);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mPost_key = getIntent().getExtras().getString("blog_id");

        String post_key = getIntent().getExtras().getString("blog_id");


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_descripcion = (String) dataSnapshot.child("descripcion").getValue();
                String post_genero = (String) dataSnapshot.child("genero").getValue();
                String post_fecha = (String) dataSnapshot.child("lanzamiento").getValue();
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();

                mBlogDescripcion.setText(post_descripcion);
                mBlogSingleTitle.setText(post_title);
                mBlogGenero.setText(post_genero);
                mBlogSingleDesc.setText(post_desc);
                mBlogLanzamiento.setText(post_fecha);


                Glide.with(GameDescription.this)

                        .load(post_image)

                        .into(mBlogSingleImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mAlquilarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPost_key = getIntent().getExtras().getString("blog_id");

                final String post_key = getIntent().getExtras().getString("blog_id");

                Intent addGameAddIntent = new Intent(GameDescription.this, AlquilerListaActivity.class);
                addGameAddIntent.putExtra("blog_id", post_key);
                startActivity(addGameAddIntent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(GameDescription.this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
