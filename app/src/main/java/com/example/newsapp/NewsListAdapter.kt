package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class NewsListAdapter(private val listener: NewsItemClicked) : ListAdapter<News, NewsListAdapter.NewsViewHolder>(NewsComparator()) {

    val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val current = items[position]
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("EEE,dd MMM")
        val output: String = formatter.format(parser.parse(current.date))

        holder.bind(output, current.imgUrl)
        Glide.with(holder.itemView.context).load(current.imgUrl).placeholder(R.drawable.noimage).into(holder.imageView)
        holder.readBtn.setOnClickListener {
            listener.onItemClicked(items[holder.adapterPosition])
        }
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val newsItemText: TextView = itemView.findViewById(R.id.item_date)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val readBtn: Button = itemView.findViewById(R.id.btn_readMore)


        fun bind(date: String?, imgUrl: String?) {
            newsItemText.text = date
            Glide.with(itemView.context).load(imgUrl).placeholder(R.drawable.noimage).into(imageView)
        }

        companion object {
            fun create(parent: ViewGroup): NewsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
                return NewsViewHolder(view)
            }
        }
    }

    class NewsComparator : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}

