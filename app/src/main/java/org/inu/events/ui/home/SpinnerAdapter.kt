//package org.inu.events.ui.home
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import androidx.annotation.LayoutRes
//import org.inu.events.databinding.UniSpinnerBinding
//import org.inu.events.databinding.UniSpinnerPopupBinding
//
//class SpinnerAdapter(
//    context: Context,
//    @LayoutRes private val resId: Int,
//    private val list: MutableList<String>,
//    private val spinnerTitle: String
//) : ArrayAdapter<String>(context, resId, list) {
//    override fun getCount(): Int = list.size
//    override fun getItem(position: Int): String = list[position]
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val binding = UniSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        binding.selectedItem.text = if (position == 0) spinnerTitle else list[position]
//
//        return binding.root
//    }
//
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val binding = UniSpinnerPopupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val model = list[position]
//        binding.item.text = model
//
//        return binding.root
//    }
//}