package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.data.*

//**Unused activity created for testing of recyclerview with offline caching **
class MainActivity : AppCompatActivity(), NewsItemClicked {

    lateinit var  mAdapter: NewsListAdapter
    private val newWordActivityRequestCode = 1
    private val newsViewModel: NewsViewModel by viewModels {
        WordViewModelFactory((application as NewsApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Add an observer on the LiveData returned by getAll().
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        newsViewModel.allNews.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { mAdapter.submitList(it) }
        }

        fetchData()

        var db= Room.databaseBuilder(applicationContext, NewsDb::class.java, "news_database").build()

        val thread = Thread {
            val newsArray = ArrayList<News>()
            val news = News("title", "url", "urlToImage", "PublishedAt")
            val news2 = News("title", "url", "urlToImage", "PublishedAt")
            newsArray.add(news)
            newsArray.add(news2)

            db.newsDao()?.insert(news)
            db.newsDao()?.insert(news2)

            Log.e("room db", "data: " + db.newsDao()?.getAll())
        }
        thread.start()

    }

 


    fun fetchData(): ArrayList<News> {
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=eec646514f5043e9bcba6bae6ce73f53"
        var newsArray = ArrayList<News>()

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val newsJsonArray = response.getJSONArray("articles");
                    Log.e("res: ", response.toString())
                    for (i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage"),
                                newsJsonObject.getString("publishedAt"),
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener { _ ->
                    Log.e("error", "Data fetch failed")
                }
        )  {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        return newsArray
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


}