package org.inu.events.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.inu.events.R
import org.inu.events.data.model.entity.Comment
import org.inu.events.databinding.ItemCommentBinding
import org.inu.events.viewmodel.CommentViewModel

class CommentAdapter(private val viewModel: CommentViewModel) :
    RecyclerView.Adapter<CommentAdapter.CommentItemViewHolder>() {

    var commentList: List<Comment> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    inner class CommentItemViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(commentModel: Comment) {
            binding.nickNameTextView.text = commentModel.nickname
            binding.commentTextView.text = commentModel.content
            binding.commentMoreButton.isVisible =  true
            binding.writerTextView.isVisible = viewModel.currentEvent.value!!.userId == commentModel.userId
            Log.i("ViewModel DSMFLKsmdkfmlSDMlfkmSLKDMfkSMDLKFMAKLFMKAMDKFMKLADFMA","${viewModel.currentEvent.value!!.userId.toString()} ${commentModel.userId.toString()}")
            // 프로필 이미지 삽입
            Glide
                .with(binding.profileImageView.context)
                .load(commentModel.profileImage)
                .fallback(R.drawable.ic_default_photo)
                .into(binding.profileImageView)


            binding.commentMoreButton.setOnClickListener{
                if (commentModel.wroteByMe == true) {
                    viewModel.showBottomSheet(commentModel.id,commentModel.userId,true)
                    Log.i("CLICK", viewModel.commentIndex.toString())
                }else{
                    viewModel.showBottomSheet(commentModel.id,commentModel.userId,false)
                    Log.i("CLICK",commentModel.userId.toString())
                }
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