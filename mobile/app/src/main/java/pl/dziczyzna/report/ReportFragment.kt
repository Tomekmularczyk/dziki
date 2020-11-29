package pl.dziczyzna.report

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pl.dziczyzna.R
import pl.dziczyzna.confirmation.ConfirmationActivity
import pl.dziczyzna.databinding.FragmentReportBinding
import pl.dziczyzna.login.presentation.model.PhotoUploadUi
import pl.dziczyzna.report.domain.model.PigCount
import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.presentation.ReportViewMode
import pl.dziczyzna.report.presentation.model.SendReportStateUi

internal class ReportFragment : Fragment() {

    private val viewModel: ReportViewMode by viewModel(parameters = {
        parametersOf(
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager,
            requireActivity().contentResolver
        )
    })
    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()

        observeGrantLocationPermissionEvent()
        observeCaptureImageEvent()
        observePhotoUploadResult()
        observeReport()
        observeSendReport()

        viewModel.fetchUserLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            viewModel.fetchUserLocation()
        }

        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.loadPhoto()
        }
    }

    private fun setupUi() {
        binding.buttonAddPhoto.setOnClickListener { viewModel.capturePhoto() }
        binding.buttonSend.setOnClickListener { viewModel.sendReport() }
    }

    private fun resetRadioListeners() {
        binding.radioAlivePig.setOnCheckedChangeListener(null)
        binding.radioDeathPig.setOnCheckedChangeListener(null)
        binding.radioSinglePig.setOnCheckedChangeListener(null)
        binding.radioMotherPig.setOnCheckedChangeListener(null)
        binding.radioHerdPig.setOnCheckedChangeListener(null)
    }

    private fun setRadioListeners() {
        binding.radioAlivePig.setOnCheckedChangeListener { _, _ ->
            viewModel.setPigType(PigType.ALIVE)
        }
        binding.radioDeathPig.setOnCheckedChangeListener { _, _ ->
            viewModel.setPigType(PigType.DEAD)
        }
        binding.radioSinglePig.setOnCheckedChangeListener { _, _ ->
            viewModel.setPigCount(PigCount.SINGLE)
        }
        binding.radioMotherPig.setOnCheckedChangeListener { _, _ ->
            viewModel.setPigCount(PigCount.MANY)
        }
        binding.radioHerdPig.setOnCheckedChangeListener { _, _ ->
            viewModel.setPigCount(PigCount.HERD)
        }
    }

    private fun observeGrantLocationPermissionEvent() {
        viewModel.grantLocationPermissionEvent().observe(viewLifecycleOwner) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun observeCaptureImageEvent() {
        viewModel.captureImageEvent().observe(viewLifecycleOwner) { imageFile ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile)
            startActivityForResult(intent, REQUEST_CAMERA_CODE)
        }
    }

    private fun observePhotoUploadResult() {
        viewModel.photoUploadResult().observe(viewLifecycleOwner) { result ->
            when (result) {
                is PhotoUploadUi.Success -> {
                    binding.progressUpload.visibility = View.GONE
                    showSnackbar(getString(R.string.sent_photo_success), null, null)
                }
                is PhotoUploadUi.Progress -> {
                    binding.progressUpload.setProgressCompat(result.progress, true)
                    binding.progressUpload.visibility = View.VISIBLE
                }
                is PhotoUploadUi.Error -> {
                    binding.progressUpload.visibility = View.GONE
                    showSnackbar(getString(R.string.sent_photo_failed), getString(R.string.try_again)) {
                        viewModel.tryAgainUploadImage()
                    }
                }
            }
        }
    }

    private fun observeSendReport() {
        viewModel.sendReportResult().observe(viewLifecycleOwner) { result ->
            when (result) {
                SendReportStateUi.InProgress -> {
                    SendingReportDialog.newInstance().show(childFragmentManager, SendingReportDialog.FRAGMENT_TAG)
                    binding.buttonSend.isEnabled = false
                }
                is SendReportStateUi.Success -> {
                    (childFragmentManager.findFragmentByTag(SendingReportDialog.FRAGMENT_TAG) as? SendingReportDialog)?.dismiss()
                    binding.buttonSend.isEnabled = false
                    startActivity(ConfirmationActivity.newInstance(requireContext(), result.city))
                }
                is SendReportStateUi.Error -> {
                    (childFragmentManager.findFragmentByTag(SendingReportDialog.FRAGMENT_TAG) as? SendingReportDialog)?.dismiss()
                    binding.buttonSend.isEnabled = true
                    showSnackbar(getString(R.string.sent_report_failed), getString(R.string.try_again)) {
                        viewModel.sendReport()
                    }
                }
            }
        }
    }

    private fun observeReport() {
        viewModel.reportViewState().observe(viewLifecycleOwner) { uiState ->
            resetRadioListeners()
            binding.textPigLocation.text = uiState.city + "\n" + uiState.state
            binding.textPigTime.text = uiState.time + "\n" + uiState.date
            binding.radioAlivePig.isChecked = uiState.type == PigType.ALIVE
            binding.radioDeathPig.isChecked = uiState.type == PigType.DEAD
            binding.radioSinglePig.isChecked = uiState.count == PigCount.SINGLE
            binding.radioMotherPig.isChecked = uiState.count == PigCount.MANY
            binding.radioHerdPig.isChecked = uiState.count == PigCount.HERD

            binding.layoutSinglePig.isEnabled = uiState.type == PigType.ALIVE
            binding.layoutMotherPig.isEnabled = uiState.type == PigType.ALIVE
            binding.layoutHerdPig.isEnabled = uiState.type == PigType.ALIVE
            binding.imagePigSingle.isEnabled = uiState.type == PigType.ALIVE
            binding.imagePigMany.isEnabled = uiState.type == PigType.ALIVE
            binding.imagePigHerd.isEnabled = uiState.type == PigType.ALIVE
            binding.textPigSingle.isEnabled = uiState.type == PigType.ALIVE
            binding.textPigMany.isEnabled = uiState.type == PigType.ALIVE
            binding.textPigHerd.isEnabled = uiState.type == PigType.ALIVE
            binding.radioSinglePig.isEnabled = uiState.type == PigType.ALIVE
            binding.radioMotherPig.isEnabled = uiState.type == PigType.ALIVE
            binding.radioHerdPig.isEnabled = uiState.type == PigType.ALIVE

            binding.layoutSinglePig.isSelected = uiState.count == PigCount.SINGLE
            binding.layoutMotherPig.isSelected = uiState.count == PigCount.MANY
            binding.layoutHerdPig.isSelected = uiState.count == PigCount.HERD

            setPigImageTint(binding.imagePigSingle, uiState.count == PigCount.SINGLE, R.drawable.ic_single)
            setPigImageTint(binding.imagePigMany, uiState.count == PigCount.MANY, R.drawable.ic_many)
            setPigImageTint(binding.imagePigHerd, uiState.count == PigCount.HERD, R.drawable.ic_herd)

            setPigTestStyle(binding.textPigSingle, uiState.count == PigCount.SINGLE)
            setPigTestStyle(binding.textPigMany, uiState.count == PigCount.MANY)
            setPigTestStyle(binding.textPigHerd, uiState.count == PigCount.HERD)

            binding.imageThumb.setImageBitmap(uiState.image)
            binding.buttonAddPhoto.isVisible = uiState.image == null

            setRadioListeners()
        }
    }

    private fun setPigImageTint(imageView: ImageView, isSelected: Boolean, @DrawableRes resource: Int) {
        val unwrappedDrawable = AppCompatResources.getDrawable(requireContext(), resource)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!).mutate()

        if (isSelected && imageView.isEnabled) {
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), R.color.color_primary))
        }

        imageView.setImageDrawable(wrappedDrawable)
    }

    private fun setPigTestStyle(textView: TextView, isSelected: Boolean) {
        textView.setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
        textView.setTypeface(null, Typeface.BOLD);

        if (isSelected && textView.isEnabled) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_primary))
        }
    }

    private fun showSnackbar(title: String, actionText: String?, action: (() -> Unit)?) {
        Snackbar.make(binding.root, title, Snackbar.LENGTH_LONG).apply {
            if (actionText != null && action != null) {
                setAction(actionText) { action() }
            }

            show()
        }
    }

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 123
        private const val REQUEST_CAMERA_CODE = 124
    }
}