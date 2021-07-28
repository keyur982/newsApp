package com.example.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsapp.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//**Unused class created for offline caching implementation**
//Room DB class

@Database(entities = arrayOf(News::class), version = 1, exportSchema = false)
abstract class NewsDb: RoomDatabase() {

    abstract fun newsDao(): NewsDao?

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var newsDao = database.newsDao()

                    // Delete all content here.
                    if (newsDao != null) {
                        newsDao.deleteAll()
                    }

                    // Add sample words.

                    //var word = News()
                    //wordDao.insert(word)

                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NewsDb? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NewsDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDb::class.java,
                    "news_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}