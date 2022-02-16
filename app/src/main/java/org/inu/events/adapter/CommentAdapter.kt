package org.inu.events.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.inu.events.BottomSheet
import org.inu.events.DetailActivity
import org.inu.events.data.model.entity.Comment
import org.inu.events.databinding.ItemCommentBinding
import org.inu.events.viewmodel.CommentViewModel

class CommentAdapter(private val viewModel: CommentViewModel) : RecyclerView.Adapter<CommentAdapter.CommentItemViewHolder>() {

    var commentList: List<Comment> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    inner class CommentItemViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.commentMoreButton.setOnClickListener(this)
        }

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

        override fun onClick(v: View) {
            // todo - 클릭 시 수정삭제 메뉴 띄우기
            viewModel.showBottomSheet()
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