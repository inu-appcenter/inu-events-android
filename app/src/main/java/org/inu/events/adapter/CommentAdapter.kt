package org.inu.events.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.inu.events.R
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemCommentBinding
import org.inu.events.viewmodel.CommentViewModel
import org.inu.events.viewmodel.DetailViewModel

class CommentAdapter(private val viewModel: CommentViewModel, private val detailViewModel: DetailViewModel) : RecyclerView.Adapter<CommentAdapter.CommentItemViewHolder>() {

    var commentList: List<Comment> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    inner class CommentItemViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(commentModel: Comment) {
            binding.nickNameTextView.text = commentModel.nickname
            binding.commentTextView.text = commentModel.content
            binding.commentMoreButton.isVisible = commentModel.wroteByMe == true
            // todo - writerTextView eventModel.wroteByMe가 true일때만 visible하도록, eventModel 변수를 어디에 해야할지? 새로 만들어야할지, 데이터를 가져와야할지
            binding.writerTextView.isVisible = detailViewModel.eventWroteByMeBoolean == true
            // 프로필 이미지 삽입
            Glide
                .with(binding.profileImageView.context)
                .load(commentModel.profileImage)
                .fallback(R.drawable.img_profile)
                .into(binding.profileImageView)


            binding.commentMoreButton.setOnClickListener{
                viewModel.showBottomSheet(commentModel.id)
                Log.i("CLICK",viewModel.commentIndex.toString())
            }
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