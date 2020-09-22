package com.azapps.newsapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.azapps.newsapp.data.Article;

public class DiffUtilArticleCallback extends DiffUtil.ItemCallback<Article> {
    @Override
    public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
        return oldItem.getArticleTitle().equals(newItem.getArticleTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
        return oldItem.getArticleUrl().equals(newItem.getArticleUrl());
    }
}
