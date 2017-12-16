package com.jesus.waiapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SignInButton mGoogleBtn;

    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private Button googleLogin;
    private static final int RC_SIGN_IN = 1;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    private DatabaseReference mDatabaseUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);


        /*
        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);
        mProgress = new ProgressDialog(this);
        */

        googleLogin = (Button) findViewById(R.id.google_login);
        mProgress = new ProgressDialog(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        /*
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        */


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("Users");

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.google_login) {
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                    }

                    googleSignIn();


                }

            }
        });




    }



    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }






/*
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
*/

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            mProgress.setMessage("Starting Sign in...");
            mProgress.show();

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                mProgress.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {

                            mProgress.dismiss();
                            checkUserExist();
                            crearUsuario();

                        }

                    }
                });
    }




    private void checkUserExist() {

        if (mAuth.getCurrentUser() !=null) {

            final String user_id = mAuth.getCurrentUser().getUid();


            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    } else {
                        Intent setupIntent = new Intent(LoginActivity.this, MainActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }


    private void crearUsuario() {


        mCurrentUser = mAuth.getCurrentUser();

        String Uid = mCurrentUser.getUid();
        String nombreUsuario = mCurrentUser.getDisplayName();
        String fotoPerfil = mCurrentUser.getPhotoUrl().toString();

        DatabaseReference childRef = mDatabaseUsers.child(Uid);
        childRef.child("uid").setValue(Uid);
        childRef.child("nombreUsuario").setValue(nombreUsuario);
        childRef.child("fotoPerfil").setValue(fotoPerfil);
    }
































}
