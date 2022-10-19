package com.rteslenko.android.doggallery.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rteslenko.android.doggallery.data.model.Breed
import com.rteslenko.android.doggallery.domain.GetBreedImageUrlsUseCase
import kotlinx.coroutines.launch

class BreedPhotoViewModel : ViewModel() {
    private var _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private var _isNextImageAvailable = MutableLiveData(false)
    val isNextImageAvailable: LiveData<Boolean> = _isNextImageAvailable

    private var currentIndex = 0
    private val imageUrls = mutableListOf<String>()

    fun notifyBreedSelected(breed: Breed) {
        viewModelScope.launch {
            val response = GetBreedImageUrlsUseCase(breed).fetch()
            when(response) {
                is GetBreedImageUrlsUseCase.Response.Success -> {
                    imageUrls.apply {
                        clear()
                        addAll(response.urls)
                    }
                    _currentImageUrl.postValue(imageUrls[currentIndex])
                    _isNextImageAvailable.postValue(currentIndex < response.urls.size - 1)
                }
                else -> {
                    _currentImageUrl.postValue("")
                    _isNextImageAvailable
                }
            }

        }
    }

    fun notifyNextImageRequested() {
        currentIndex++
        if (currentIndex < imageUrls.size) {
            _currentImageUrl.value = imageUrls[currentIndex]
        }
        if (currentIndex >= imageUrls.size - 1) {
            _isNextImageAvailable.value = false
        }
    }
}