package com.jesus.waiapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.subinkrishna.widget.CircularImageView;

public class AccountActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth.AuthStateListener mAuthlistener;
    private ImageView fondoPerfil;

    private ImageView mFotoListaAlquiler;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseJuegos;
    private String mPost_key = null;
    private TextView mNombreJuego;
    private FirebaseUser mCurrentUser;
    private String mPaquito;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsuario;
    private CircularImageView mFotoPerfilAlquiler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        fondoPerfil = (ImageView) findViewById(R.id.fondoPerfil);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage ( this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi (Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();



        // mBlogList.setNestedScrollingEnabled(false);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mPaquito = mCurrentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mPaquito).child("Games");
        mBlogList = (RecyclerView)findViewById(R.id.mis_juegos_lista);

        mNombreJuego = (TextView) findViewById(R.id.nombreJuego);
        // mFotoPerfilAlquiler = (CircularImageView) findViewById(R.id.fotoPerfilAlquiler);

        // mTituloListaAlquiler

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

        FirebaseRecyclerAdapter<Blog, AccountActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, AccountActivity.BlogViewHolder>(
                Blog.class,
                R.layout.blog_row_lista_account_games,
                AccountActivity.BlogViewHolder.class,
                mDatabase

        ){
            @Override
            protected void populateViewHolder (AccountActivity.BlogViewHolder viewHolder,  Blog model, int position){


                viewHolder.setNombreJuego(model.getNombreJuego());




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

        public void setNombreJuego (String nombreJuego){
            TextView post_nombreUsuario = (TextView) mView.findViewById(R.id.nombreJuego);
            post_nombreUsuario.setText(nombreJuego);
        }



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

    }
}