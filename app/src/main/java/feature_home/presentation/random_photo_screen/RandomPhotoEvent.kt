package feature_home.presentation.random_photo_screen

import com.example.astronomicalphotooftheday.domain.model.Photo


sealed class RandomPhotoEvent {
    data class AddButtonClicked(val photo: Photo) : RandomPhotoEvent()
}
interface DialogListener {
    fun onSelect(date: String?)
    fun getDate(): String?
}