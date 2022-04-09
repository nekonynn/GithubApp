package tech.nekonyan.githubapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tech.nekonyan.githubapp.data.model.GithubUser
import tech.nekonyan.githubapp.databinding.ItemUserBinding
import tech.nekonyan.githubapp.ui.activity.DetailActivity
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.Extensions.setPlaceholderAndError

class GithubUserAdapter(private val context: Context, private val isFavoriteList: Boolean = false) :
    ListAdapter<GithubUser, GithubUserAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(
        private val context: Context,
        private val isFavoriteList: Boolean,
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GithubUser) {
            binding.item = item
            Glide.with(context).load(item.avatar)
                .setPlaceholderAndError(context)
                .into(binding.ivAvatar)
            binding.root.setOnClickListener {
                Intent(context, DetailActivity::class.java).run {
                    putExtra(Constants.USERNAME_EXTRA, item.username)
                    putExtra(Constants.FAVORITE_FLAG_EXTRA, isFavoriteList)
                    context.startActivity(this)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context, isFavoriteList,
            ItemUserBinding.inflate(LayoutInflater.from(context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    val listItem: ArrayList<GithubUser> = ArrayList()

    fun setDataList(list: List<GithubUser>) {
        listItem.clear()
        listItem.addAll(list)
        notifyDataSetChanged()
    }
}