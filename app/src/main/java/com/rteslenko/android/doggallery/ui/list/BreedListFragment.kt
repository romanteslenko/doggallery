package com.rteslenko.android.doggallery.ui.list

import android.graphics.Outline
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rteslenko.android.doggallery.R
import com.rteslenko.android.doggallery.databinding.FragmentBreedListBinding
import com.rteslenko.android.doggallery.ui.list.BreedListViewModel.DataStatus.*

class BreedListFragment : Fragment() {

    private val viewModel by viewModels<BreedListViewModel>()

    private var _binding: FragmentBreedListBinding? = null
    private val binding get() = _binding!!

    private val recyclerAdapter by lazy {
        BreedListAdapter() { breed ->
            val name = breed.name.replaceFirstChar {
                it.uppercase()
            }
            val action = BreedListFragmentDirections.actionListFragmentToPhotoFragment(breed, name)
            try {
                findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e(BreedListFragment::class.simpleName, e.toString())
            }
        }
    }

    private val connectivityManager by lazy {
        getSystemService(requireContext(), ConnectivityManager::class.java)
    }
    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModel.requestBreedList()
                connectivityManager?.unregisterNetworkCallback(this)
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {}

            override fun onLost(network: Network) {}
        }
    }
    private var isNetworkCallbackRegistered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeData()
        observeStatus()
    }

    private fun setupRecycler() {
        with(binding.recycler) {
            val orientation = RecyclerView.VERTICAL
            layoutManager = LinearLayoutManager(requireContext(), orientation, false)
            adapter = recyclerAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), orientation))
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, 50f)
                }
            }
            clipToOutline = true
        }
    }

    private fun observeData() {
        viewModel.breedList.observe(viewLifecycleOwner) { breedList ->
            recyclerAdapter.submitList(breedList)
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.progressBar.visibility = if (status == LOADING) {
                View.VISIBLE
            } else {
                View.GONE
            }
            with(binding.noData) {
                if (status == ERROR) {
                    visibility = View.VISIBLE
                    text = if (hasInternet()) {
                        getString(R.string.no_data)
                    } else {
                        registerNetworkCallback()
                        getString(R.string.no_connection)
                    }
                } else {
                    visibility = View.GONE
                }
            }
        }
    }

    private fun hasInternet(): Boolean {
        val networkCapabilities = connectivityManager?.let { manager ->
            manager.activeNetwork?.let { network ->
                manager.getNetworkCapabilities(network)
            }
        }
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED)
    }

    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addCapability(NET_CAPABILITY_VALIDATED)
            .build()
        connectivityManager?.apply {
            registerNetworkCallback(networkRequest, networkCallback)
            isNetworkCallbackRegistered = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (isNetworkCallbackRegistered) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}