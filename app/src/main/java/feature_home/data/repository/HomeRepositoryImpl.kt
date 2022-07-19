package feature_home.data.repository

import com.example.core.utils.Resource
import com.example.astronomicalphotooftheday.data.remote.HomeApi
import com.example.astronomicalphotooftheday.domain.model.Photo
import feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(
    private val api: HomeApi
) : HomeRepository {

    override fun getTodayPhoto(): Flow<Resource<Photo>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(api.getTodayPhoto().toPhotoDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }

    override fun getPhotoByDate(date:String?): Flow<Resource<Photo>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(api.getPhotoByDate(date).toPhotoDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}