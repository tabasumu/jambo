package com.mambobryan.jambo.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mambobryan.jambo.databinding.BottomSheetMoreBinding
import com.mambobryan.jambo.helpers.getColor
import com.mambobryan.jambo.helpers.getFullTag
import com.mambobryan.jambo.helpers.setupFullHeight
import kotlinx.coroutines.flow.collectLatest

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/13/22 at 7:32 PM
 */
class MoreBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JamboViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog

            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.setupFullHeight()
        }

        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenResumed {
            viewModel.selectedLog.collectLatest {
                if (it != null)
                    binding.apply {
                        toolbarMore.title = it.getFullTag()
                        toolbarMore.setNavigationOnClickListener { dismiss() }
                        tvMore.text = it.message

                        layoutMoreTag.setBackgroundColor(Color.parseColor(it.getColor().second))
                    }
            }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.selectLog(null)
    }

}