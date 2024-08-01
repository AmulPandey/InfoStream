package com.example.mynews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynews.data.database.dao.ArticleDao
import com.example.mynews.data.database.dao.SavedArticleDao
import com.example.mynews.data.database.entity.Article
import com.example.mynews.data.database.entity.SavedArticleEntity

@Database(entities = [SavedArticleEntity::class, Article::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getSavedArticleDao(): SavedArticleDao
    abstract fun getArticleDao(): ArticleDao

}