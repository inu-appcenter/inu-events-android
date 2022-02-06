package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.inu.events.data.CommentData
import org.inu.events.databinding.ItemCommentBinding

class CommentAdapter(private var commentList: LiveData<List<CommentData>>) : RecyclerView.Adapter<CommentAdapter.CommentItemViewHolder>() {
    inner class CommentItemViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(commentModel: CommentData) {
            System.out.println("bind")
            binding.nickNameTextView.text = commentModel.nickName
            binding.commentTextView.text = commentModel.comment
            // 프로필 이미지 삽입
            Glide
                .with(binding.profileImageView.context)
                .load(commentModel.profile)
                .into(binding.profileImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder {
        return CommentItemViewHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentItemViewHolder, position: Int) {
        holder.bind(commentList.value!![position])
    }

    override fun getItemCount(): Int = commentList.value!!.size
    //override fun getItemCount() = commentList.value?.size ?: 0
}