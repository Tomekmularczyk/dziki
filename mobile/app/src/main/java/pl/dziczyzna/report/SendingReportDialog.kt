package pl.dziczyzna.report

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.dziczyzna.R

internal class SendingReportDialog : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(LayoutInflater.from(requireContext()).inflate(R.layout.dialog_progress, null))
            .setCancelable(false)
            .create()
    }

    companion object {

        const val FRAGMENT_TAG = "SendReporyDialog"

        fun newInstance() = SendingReportDialog()
    }
}