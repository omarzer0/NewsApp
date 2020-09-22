package com.azapps.newsapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azapps.newsapp.R;
import com.azapps.newsapp.adapter.ArticleAdapter;
import com.azapps.newsapp.adapter.OnArticleClickListener;
import com.azapps.newsapp.data.Article;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnArticleClickListener {
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiViews();
        addFakeData();
        initRecyclerView();
    }

    private void intiViews() {
        recyclerView = findViewById(R.id.activity_recycler_view_rv);
    }

    private void addFakeData() {
        articleList = new ArrayList<>();
        articleList.add(new Article("one", "football","2020-10-11",
                "https://www.theguardian.com/football/2020/sep/16/football-league-alarm-premier-league-help-save-clubs-from-ruin"));
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter(this, this);
        adapter.submitList(articleList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onArticleClick(int position) {
        Uri uri = Uri.parse(articleList.get(position).getArticleUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(webIntent);
    }
}