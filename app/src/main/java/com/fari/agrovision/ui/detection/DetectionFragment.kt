package com.fari.agrovision.ui.detection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fari.agrovision.R
import com.fari.agrovision.data.remote.model.detection.PlantDisease
import com.fari.agrovision.data.remote.model.detection.Ripeness
import com.fari.agrovision.databinding.FragmentDetectionBinding
import com.fari.agrovision.ui.camera.CameraActivity
import com.fari.agrovision.ui.detection.adapter.ListDiseaseAdapter
import com.fari.agrovision.ui.detection.adapter.ListRipenessAdapter
import com.fari.agrovision.data.local.Result
import com.fari.agrovision.ui.auth.AuthViewModel
import com.fari.agrovision.ui.auth.AuthViewModelFactory
import com.fari.agrovision.ui.auth.signup.SignUpActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetectionFragment : Fragment() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireContext().dataStore)
    }
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
    private var model = "PD-apple"
    private var title = "Apel"
    private val detectionViewModel: DetectionViewModel by viewModels{
        DetectionViewModelFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isLogin().observe(requireActivity()) { uid ->
            if (uid.isNullOrEmpty()) {
                navigateToSignup()
            }
        }

        setupAction()
    }

    private fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    private fun setupAction() {
        detectionViewModel.getListDetectionDisease().observe(requireActivity()){result->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val listDisease = result.data
                    setPlantDiseaseData(listDisease)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Failed to load detection list",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        detectionViewModel.getListDetectionRipeness().observe(requireActivity()){result->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val listRipeness = result.data
                    setFruitRipenessData(listRipeness)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    checkIfFragmentAttached {
                        Toast.makeText(
                            requireActivity(),
                            "Failed to load detection list",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun setPlantDiseaseData(listDisease: List<PlantDisease>) {
        val adapter = ListDiseaseAdapter(listDisease)

        adapter.setOnItemClickCallback(object : ListDiseaseAdapter.OnItemClickCallback {
            override fun onItemDiseaseClicked(item: PlantDisease) {
                model = item.model
                title = item.title
                if (!allPermissionsGranted()) ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
                else startCameraX()
            }
        })

        binding.apply {
            rvPlant.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvPlant.layoutManager = layoutManager
        }

    }

    private fun setFruitRipenessData(listRipeness: List<Ripeness>) {
        val adapter = ListRipenessAdapter(listRipeness)

        adapter.setOnItemClickCallback(object : ListRipenessAdapter.OnItemClickCallback {
            override fun onItemRipenessClicked(item: Ripeness) {
                model = item.model
                title = item.title
                if (!allPermissionsGranted()) ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
                else startCameraX()
            }
        })

        binding.apply {
            rvFruit.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvFruit.layoutManager = layoutManager
        }

    }

    private fun navigateToSignup() {
        checkIfFragmentAttached {
            Toast.makeText(
                requireContext(),
                getString(R.string.logout),
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.allow_permission),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                startCameraX()
            }
        }
    }


    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        intent.putExtra(CameraActivity.IS_DETECTION, true)
        intent.putExtra(CameraActivity.MODEL, model)
        intent.putExtra(CameraActivity.TITLE, title)
        startActivity(intent)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}