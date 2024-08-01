package com.example.mynews.di.module

import android.app.Application
import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import androidx.work.WorkManager
import com.example.mynews.Common.Const
import com.example.mynews.Common.dispatcher.DefaultDispatcherProvider
import com.example.mynews.Common.dispatcher.DispatcherProvider
import com.example.mynews.Common.logger.AppLogger
import com.example.mynews.Common.logger.Logger
import com.example.mynews.Common.networkhelper.NetworkHelper
import com.example.mynews.Common.networkhelper.NetworkHelperImpl
import com.example.mynews.data.database.AppDatabaseService
import com.example.mynews.data.database.ArticleDatabase
import com.example.mynews.data.database.DatabaseService
import com.example.mynews.data.database.entity.Article
import com.example.mynews.data.network.ApiInterface
import com.example.mynews.data.network.ApiKeyInterceptor
import com.example.mynews.di.ApiKey
import com.example.mynews.di.BaseUrl
import com.example.mynews.di.DbName
import com.example.mynews.ui.whole.paging.NewsPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(
        application: Application,
        @DbName dbName: String
    ): ArticleDatabase {
        return Room.databaseBuilder(
            application,
            ArticleDatabase::class.java,
            dbName
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @ApiKey
    @Provides
    fun provideApiKey(): String = Const.API_KEY

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = Const.BASE_URL

    @DbName
    @Provides
    fun provideDbName(): String = Const.DB_NAME

    @Singleton
    @Provides
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonFactory: GsonConverterFactory,
        apiKeyInterceptor: ApiKeyInterceptor
    ): ApiInterface {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

        return Retrofit
            .Builder()
            .client(client) //adding client to intercept all responses
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger()

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun providePager(
        newsPagingSource: NewsPagingSource
    ): Pager<Int, Article> {
        return Pager(
            config = PagingConfig(
                Const.DEFAULT_QUERY_PAGE_SIZE
            )
        ) {
            newsPagingSource
        }
    }

    @Provides
    @Singleton
    fun provideNetworkHelper(
        @ApplicationContext context: Context
    ): NetworkHelper {
        return NetworkHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseService(articleDatabase: ArticleDatabase): DatabaseService {
        return AppDatabaseService(articleDatabase)
    }

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager {
        return WorkManager.getInstance(context)
    }

}