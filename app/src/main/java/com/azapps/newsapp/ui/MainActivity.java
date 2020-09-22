package com.azapps.newsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azapps.newsapp.R;
import com.azapps.newsapp.adapter.ArticleAdapter;
import com.azapps.newsapp.adapter.OnArticleClickListener;
import com.azapps.newsapp.data.Article;
import com.azapps.newsapp.utils.ArticleLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnArticleClickListener,
        LoaderManager.LoaderCallbacks<List<Article>> {


    private static String ARTICLE_API;

    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ArrayList<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initURI();
        intiViews();
        initRecyclerView();
        checkForConnection();
    }

    private void initURI() {
        Uri.Builder builder = new Uri.Builder();
        // this is the api I used
        // "https://content.guardianapis.com/search?api-key=test&section=football"
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("api-key", "test")
                .appendQueryParameter("section", "football");
        ARTICLE_API = builder.build().toString();
    }

    private void checkForConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }

    private void intiViews() {
        progressBar = findViewById(R.id.activity_main_progress_bar_pb);
        emptyTextView = findViewById(R.id.activity_main_empty_text_tv);
        recyclerView = findViewById(R.id.activity_recycler_view_rv);
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onArticleClick(int position) {
        Uri uri = Uri.parse(articleList.get(position).getArticleUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }

    @NonNull
    @Override
    public Loader<List<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ArticleLoader(this, ARTICLE_API);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> data) {
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            articleList.addAll(data);
        } else {
            emptyTextView.setText(R.string.no_article_available);
        }
        adapter.submitList(articleList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Article>> loader) {
        loader.reset();
    }
}