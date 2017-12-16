package com.jesus.waiapp;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_requests,container,false);
        mBlogList = (RecyclerView) view.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBlogList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBlogList.getContext(),
                layoutManager.getOrientation());
        mBlogList.addItemDecoration(dividerItemDecoration);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        return view;


    }





    @Override
    public void onStart() {
        super.onStart();

        Query query = mDatabase.orderByChild("valoracion");

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                query
        ){
            @Override
            protected void populateViewHolder ( BlogViewHolder viewHolder, Blog model, int position){
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(), model.getImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleBlogIntent = new Intent(getActivity(), GameDescription.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
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

        public void setTitle (String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc (String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);



            Glide.with(ctx)

                    .load(image)
                    .into(post_image);

        }
    }




}