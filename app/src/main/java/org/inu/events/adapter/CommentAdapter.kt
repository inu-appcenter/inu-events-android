package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.inu.events.data.model.entity.Comment
import org.inu.events.databinding.ItemCommentBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentItemViewHolder>() {

    var commentList: List<Comment> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    inner class CommentItemViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(commentModel: Comment) {
            binding.nickNameTextView.text = commentModel.nickName
            binding.commentTextView.text = commentModel.content
            binding.commentMoreButton.isVisible = commentModel.wroteByMe == true

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
        holder.bind(commentList[position])
    }

    override fun getItemCount(): Int = commentList.size
}