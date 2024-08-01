package com.example.mynews.Common.util

import com.example.mynews.data.database.entity.Article


fun List<Article>.filterArticles(): List<Article> {
    return this.filterNot { article ->
        article.title.isNullOrEmpty() || article.description.isNullOrEmpty() || article.urlToImage.isNullOrEmpty()
    }
}