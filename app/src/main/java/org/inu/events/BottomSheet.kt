package org.inu.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.inu.events.databinding.FragmentBottomSheetBinding
import org.inu.events.viewmodel.CommentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentBottomSheetBinding
    private val commentViewModel: CommentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
    }

    private fun initButton() {
        binding.editButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.editButton -> {
                Log.i("Button","edit click")
                // todo - 수정 부분 만들고 여기에 넣기
            }
            R.id.deleteButton -> {
                Log.i("Button","delete click")
                // todo - 삭제 부분 commentId는 어떻게?
                //commentViewModel.deleteComment(commentId = )
            }
            R.id.cancelButton -> {
                Log.i("Button","cancel click")
                // todo - dismiss ? cancel?
                //onCancel(binding)
            }
        }
    }


}