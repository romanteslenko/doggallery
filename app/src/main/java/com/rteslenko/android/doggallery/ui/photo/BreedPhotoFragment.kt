package com.rteslenko.android.doggallery.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.rteslenko.android.doggallery.databinding.FragmentBreedPhotoBinding
import com.rteslenko.android.doggallery.utils.ImageLoader

class BreedPhotoFragment : Fragment() {

    private val viewModel by viewModels<BreedPhotoViewModel>()
    private val args: BreedPhotoFragmentArgs by navArgs()

    private var _binding: FragmentBreedPhotoBinding? = null
    private val binding get() = _binding!!

    private val imageLoader by lazy { ImageLoader(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextPhotoButton.setOnClickListener {
            viewModel.notifyNextImageRequested()
        }

        with(viewModel) {
            notifyBreedSelected(args.breed)
            currentImageUrl.observe(viewLifecycleOwner) { url ->
                imageLoader.load(url, binding.dogImage)
            }
            isNextImageAvailable.observe(viewLifecycleOwner) { isAvailable ->
                binding.nextPhotoButton.isEnabled = isAvailable
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}