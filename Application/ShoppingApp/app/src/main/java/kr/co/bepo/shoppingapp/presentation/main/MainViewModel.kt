package kr.co.bepo.shoppingapp.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.shoppingapp.presentation.BaseViewModel

internal class MainViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch {  }

    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData: MutableLiveData<MainState> = _mainStateLiveData

    fun refreshOrderList() {
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}