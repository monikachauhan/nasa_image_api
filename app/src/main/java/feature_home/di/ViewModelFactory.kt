package feature_home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import feature_home.presentation.random_photo_screen.RandomPhotoViewModel

class ViewModelFactory  constructor(val sampleViewmodel: RandomPhotoViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if(modelClass == RandomPhotoViewModel::class.java){
            sampleViewmodel as T
        }else{
            throw  IllegalStateException("Unknown entity")
        }
}