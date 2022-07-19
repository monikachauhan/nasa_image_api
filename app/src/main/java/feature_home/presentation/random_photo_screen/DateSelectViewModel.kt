package feature_home.presentation.random_photo_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astronomicalphotooftheday.domain.model.Photo
import feature_home.domain.repository.HomeRepository
import com.example.core.utils.Resource
import com.example.feature_favorites.favorites_screen.domain.use_case.InsertPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DateSelectViewModel @Inject constructor(
) : ViewModel() {

    private val _randomPhotos = MutableLiveData<Photo>()
    val randomPhotos: LiveData<Photo>
        get() = _randomPhotos

    private val _randomPhotosChanel = Channel<UiEvent>()
    val randomPhotosEvent = _randomPhotosChanel.receiveAsFlow()

   /* private fun getPhotoByDate() {
        viewModelScope.launch {
            repository.getPhotoByDate("2016-11-04").onEach { event ->
                when (event) {
                    is Resource.Loading -> {
                        _randomPhotosChanel.send(UiRandomPhotoEvent.ShowProgressBar(true))
                    }
                    is Resource.Success -> {
                        event.data?.let { photos ->
                            _randomPhotos.postValue(photos)
                            _randomPhotosChanel.send(UiRandomPhotoEvent.ShowProgressBar(false))
                        }
                    }
                    is Resource.Error -> {
                        _randomPhotosChanel.send(UiRandomPhotoEvent.ShowSnackbar("${event.message}"))
                    }
                }
            }.launchIn(this)
        }
    }

    init {
        getPhotoByDate()
    }*/

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ShowDate -> {
                viewModelScope.launch {

                  //  insertPhotoUseCase(event.photo.toFavoritesPhoto()) // refactor
                    //_randomPhotosChanel.send(UiRandomPhotoEvent.ShowSnackbar("Successfully added"))
                }
            }
        }
    }

    fun formatDate(year: Int, month: Int, day: Int): String {
        val mon = month + 1
        var currentDateString = ""
        var dayString = "" + day
        if ( day < 10) {
            dayString = "0$day"
        }
        currentDateString = if (mon < 10) {
            val mm = "0$mon"
            (year.toString() + "-" + mm + "-"
                    + dayString)
        } else {
            (year.toString() + "-" + mon + "-"
                    + dayString)
        }
        return currentDateString;

    }

    sealed class UiEvent {
        data class ShowDate(val year: Int,val month: Int,val day: Int): UiEvent()
    }
}