package feature_home.domain.repository

import com.example.astronomicalphotooftheday.domain.model.Photo
import com.example.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getTodayPhoto(): Flow<Resource<Photo>>

    fun getPhotoByDate(date: String?): Flow<Resource<Photo>>
}