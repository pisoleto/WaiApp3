package com.jesus.waiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.subinkrishna.widget.CircularImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG =null;

    private Context context_Alq;
    private DatabaseReference mUserRef;

    private TextView mNombreUsuarioNavigation;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mDatabaseUsers;
    private GoogleApiClient mGoogleApiClient;
    private ImageView fotoNavigation;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    private TabLayout tabLayout;
    private  int [] tabIcons = {
            R.mipmap.ic_whatshot_white_24dp,
            R.mipmap.ic_cake_white_24dp,
            R.mipmap.ic_place_white_24dp

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.logo_wai2);
        menu.setDisplayUseLogoEnabled(true);



        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users"); // Creación de carpeta "Blog" en la BD
        mDatabaseUsers.keepSynced(true); // Mantener sincronizado cuando se pierda la conexión

        mAuth = FirebaseAuth.getInstance();








        //AUN NO SE SI FUNCIONA, ES PARA VER EL ONLINEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE




        if (mAuth.getCurrentUser() != null) {


            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());


            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //cuando se desconecte con el logout que cree un nuevo hijo en la base de datos que se llame online

                    if (dataSnapshot != null) {
                        mUserDatabase.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }




        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Si el usuario no esta registrado manda para la actividad Register

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddGameSearchIntent = new Intent(MainActivity.this, AddGameSearchPhase.class);
                AddGameSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(AddGameSearchIntent);
            }
        });







        getSupportActionBar().setDisplayShowTitleEnabled(false);


        viewPager= (ViewPager) findViewById(R.id.container);

        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        setupTabIcons();


        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);


        CircularImageView fotoNavigation = (CircularImageView) headerView.findViewById(R.id.fotoNavigation);
        fotoNavigation.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mNombreUsuarioNavigation = (TextView)headerView.findViewById(R.id.nombreUsuarioNavigation);



        String nombreUsuario = null;
        if (mCurrentUser != null) {
            nombreUsuario = mCurrentUser.getDisplayName();
        }
        Uri fotoPerfil = null;
        if (mCurrentUser != null) {
            fotoPerfil = mCurrentUser.getPhotoUrl();
        }


        mNombreUsuarioNavigation.setText(nombreUsuario);










        Glide.with(getApplicationContext())
                .load(fotoPerfil)
                .asBitmap()

                .into(fotoNavigation);
















        fotoNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FotoIntent = new Intent(MainActivity.this, AccountActivity.class);
                FotoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(FotoIntent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mAuth.addAuthStateListener(mAuthlistener);



    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#1e564f"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#1e564f"), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#1e564f"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
