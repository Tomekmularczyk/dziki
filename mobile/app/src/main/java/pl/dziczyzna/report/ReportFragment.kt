package pl.dziczyzna.report

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pl.dziczyzna.databinding.FragmentReportBinding
import pl.dziczyzna.report.domain.model.PigCount
import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.presentation.ReportViewMode

internal class ReportFragment : Fragment() {

    private val viewModel: ReportViewMode by viewModel(parameters = {
        parametersOf(requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    })
    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeGrantLocationPermissionEvent()
        observeReport()

        viewModel.fetchUserLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            viewModel.fetchUserLocation()
        }
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
            binding.radioSinglePig.isClickable = uiState.type == PigType.ALIVE
            binding.radioMotherPig.isClickable = uiState.type == PigType.ALIVE
            binding.radioHerdPig.isClickable = uiState.type == PigType.ALIVE
            setRadioListeners()
        }
    }

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 123
    }
}