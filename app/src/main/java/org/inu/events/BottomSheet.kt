package org.inu.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.inu.events.databinding.FragmentBottomSheetBinding
import org.inu.events.viewmodel.CommentViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentBottomSheetBinding
    private val commentViewModel: CommentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
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
                Log.i("Button", "edit click")
                commentViewModel.updateComment()
            }
            R.id.deleteButton -> {
                Log.i("Button", "delete click")
                commentViewModel.deleteComment{
                    dismiss()
                }
            }
            R.id.cancelButton -> {
                Log.i("Button", "cancel click")
                dismiss()
            }
        }
    }


}