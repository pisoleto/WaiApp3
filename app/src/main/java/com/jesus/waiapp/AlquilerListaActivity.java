package com.jesus.waiapp;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.subinkrishna.widget.CircularImageView;

public class AlquilerListaActivity extends AppCompatActivity {

    private CircularImageView mFotoPerfilAlquiler;

    private ImageView mFotoListaAlquiler;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseJuegos;
    private String mPost_key = null;
    private TextView mTituloListaAlquiler;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alquiler_lista);

        mPost_key = getIntent().getExtras().getString("blog_id");

        final String post_key = getIntent().getExtras().getString("blog_id");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Alquilados").child(post_key);
        mBlogList = (RecyclerView)findViewById(R.id.alquiler_list);
        mFotoListaAlquiler = (ImageView)findViewById(R.id.foto_lista_alquiler);
        mTituloListaAlquiler = (TextView) findViewById(R.id.titulo_lista_alquiler);
        mFotoPerfilAlquiler = (CircularImageView) findViewById(R.id.fotoPerfilAlquiler);







        mDatabaseJuegos = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabaseJuegos.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_tituloListaAlquiler = (String) dataSnapshot.child("title").getValue();

                String post_image = (String) dataSnapshot.child("image").getValue();

                mTituloListaAlquiler.setText(post_tituloListaAlquiler);
                Glide.with(AlquilerListaActivity.this)

                        .load(post_image)

                        .into(mFotoListaAlquiler);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mBlogList.setNestedScrollingEnabled(false);








        mBlogList.setHasFixedSize(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mBlogList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBlogList.getContext(),
                layoutManager.getOrientation());
        mBlogList.addItemDecoration(dividerItemDecoration);



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, AlquilerListaActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, AlquilerListaActivity.BlogViewHolder>(
                Blog.class,
                R.layout.blog_row_lista_alquiler,
                AlquilerListaActivity.BlogViewHolder.class,
                mDatabase

        ){
            @Override
            protected void populateViewHolder (AlquilerListaActivity.BlogViewHolder viewHolder,  Blog model, int position){

                final String user_id = getRef(position).getKey();

                viewHolder.setNombreUsuario(model.getNombreUsuario());
                viewHolder.setPrecioAlquiler(model.getPrecioAlquiler());
                viewHolder.setFotoPerfil(getApplicationContext(), model.getFotoPerfil());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileRentIntent = new Intent(AlquilerListaActivity.this, ProfileRentActivity.class);
                        profileRentIntent.putExtra("user_id", user_id);
                        startActivity(profileRentIntent);
                    }
                });



            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNombreUsuario (String nombreUsuario){
            TextView post_nombreUsuario = (TextView) mView.findViewById(R.id.post_nombreUsuario);
            post_nombreUsuario.setText(nombreUsuario);
        }

        public void setPrecioAlquiler (String precioAlquiler){
            TextView post_precioAlquiler = (TextView) mView.findViewById(R.id.post_precioAlquiler);
            post_precioAlquiler.setText(precioAlquiler);
        }

        public void setFotoPerfil(Context ctx_alq, String fotoPerfil) {

            CircularImageView post_image = (CircularImageView) mView.findViewById(R.id.fotoPerfilAlquiler);



            Glide.with(ctx_alq)
                    .load(fotoPerfil)
                    .asBitmap()

                    .into(post_image);

        }




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
            startActivity(new Intent(AlquilerListaActivity.this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
