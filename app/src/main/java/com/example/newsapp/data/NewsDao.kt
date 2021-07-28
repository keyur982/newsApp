package com.example.newsapp.data

import androidx.room.*
import com.example.newsapp.News
import kotlinx.coroutines.flow.Flow

//**Unused class created for offline caching implementation**
// Data Access Object Interface
@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAll(): Flow<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: ArrayList<News>)

    @Query("DELETE FROM news")
    fun deleteAll()

}