package com.example.eziketobenna.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //set layout manager to the recycler view
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(mGridLayoutManager);

    }
}
