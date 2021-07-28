package com.example.newsapp.data

import androidx.annotation.WorkerThread
import com.example.newsapp.News
import kotlinx.coroutines.flow.Flow

//**created for offline caching implementation**

class NewsRepository(private val newsDao: NewsDao?) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allNews: Flow<List<News>> = newsDao!!.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(news: ArrayList<News>) {
        newsDao?.insertAll(news)
    }
}
