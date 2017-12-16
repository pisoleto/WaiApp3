package com.jesus.waiapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

    private ImageView mImage;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList = (RecyclerView)findViewById(R.id.blog_list);

        mBlogList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mBlogList.setLayoutManager(layoutManager);








    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row_search,
                BlogViewHolder.class,
                mDatabase

        ){
            @Override
            protected void populateViewHolder ( final BlogViewHolder viewHolder,  final Blog model,  int position){



                viewHolder.setTitle(model.getTitle());

            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }



















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Buscar...");





        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor (Color.TRANSPARENT);
            int searchTextId = searchPlate.getContext ().getResources ().getIdentifier ("android:id/search_src_text", null, null);

        }


























        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Query Q = mDatabase.child("Blog").orderByChild("title").startAt(newText).endAt(newText+"\uf8ff");

                FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                        Blog.class,
                        R.layout.blog_row_search,
                        BlogViewHolder.class,
                        Q

                ){
                    @Override
                    protected void populateViewHolder ( final BlogViewHolder viewHolder,  final Blog model,  int position){

                        final String post_key = getRef(position).getKey();


                        viewHolder.setTitle(model.getTitle());


                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent singleBlogIntent = new Intent(SearchActivity.this, GameDescription.class);
                                singleBlogIntent.putExtra("blog_id", post_key);
                                startActivity(singleBlogIntent);
                            }
                        });



                    }
                };

                mBlogList.setAdapter(firebaseRecyclerAdapter);

                return false;
            }

        });




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle (String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

    }

}



