package com.azapps.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.azapps.newsapp.R;
import com.azapps.newsapp.data.Article;

public class ArticleAdapter extends ListAdapter<Article, ArticleViewHolder> {
    OnArticleClickListener listener;
    private Context context;
    private int lastPosition = -1;

    private static final DiffUtilArticleCallback  UTIL_ARTICLE_CALLBACK = new DiffUtilArticleCallback();
    public ArticleAdapter(Context context, OnArticleClickListener onArticleClickListener) {
        super(UTIL_ARTICLE_CALLBACK);
        this.context = context;
        listener = onArticleClickListener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article currentArticle = getItem(position);
        holder.articleTitle.setText(currentArticle.getArticleTitle());
        holder.sectionName.setText(currentArticle.getSectionName());
        holder.authorName.setText(currentArticle.getAuthorName());
        holder.publishDate.setText(currentArticle.getPublishDate());
        setAnimation(holder.cardView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // This is something i like and i hope you like it too :)
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
