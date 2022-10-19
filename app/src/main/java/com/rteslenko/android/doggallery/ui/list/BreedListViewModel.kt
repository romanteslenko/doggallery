package com.rteslenko.android.doggallery.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rteslenko.android.doggallery.data.model.Breed
import com.rteslenko.android.doggallery.domain.GetBreedListUseCase
import com.rteslenko.android.doggallery.domain.GetBreedListUseCase.Result.Success
import kotlinx.coroutines.launch

class BreedListViewModel : ViewModel() {

    enum class DataStatus {
        LOADING, ERROR, DONE
    }

    private var _breedList = MutableLiveData<List<Breed>>(emptyList())
    val breedList: LiveData<List<Breed>> = _breedList

    private var _status = MutableLiveData(DataStatus.LOADING)
    val status: LiveData<DataStatus> = _status

    init {
        requestBreedList()
    }

    fun requestBreedList() {
        viewModelScope.launch {
            when(val result = GetBreedListUseCase().fetch()) {
                is Success -> {
                    _breedList.postValue(result.breedList)
                    _status.postValue(DataStatus.DONE)
                }
                else -> {
                    _breedList.postValue(emptyList())
                    _status.postValue(DataStatus.ERROR)
                }
            }
        }
    }
}