package com.example.newsapp.data

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//**Unused class created for offline caching implementation**
class NewsApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { NewsDb.getDatabase(this, applicationScope)}
    val repository by lazy { NewsRepository(database.newsDao()) }
}
