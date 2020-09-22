package com.azapps.newsapp.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azapps.newsapp.R;

public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView articleTitle;
    TextView sectionName;
    TextView authorName;
    TextView publishDate;
    CardView cardView;
    OnArticleClickListener listener;

    public ArticleViewHolder(@NonNull View itemView, OnArticleClickListener onArticleClickListener) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.item_article_article_title_tv);
        sectionName = itemView.findViewById(R.id.item_article_section_name_tv);
        authorName = itemView.findViewById(R.id.item_article_author_name);
        publishDate = itemView.findViewById(R.id.item_article_publish_date);
        cardView = itemView.findViewById(R.id.item_article_card_view_cv);
        itemView.setOnClickListener(this);
        listener = onArticleClickListener;
    }

    @Override
    public void onClick(View view) {
        listener.onArticleClick(getAdapterPosition());
    }
}
