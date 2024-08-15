package com.example.moviebox.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moviebox.util.constant.DatabaseConstants.MOVIE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM $MOVIE_TABLE")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM $MOVIE_TABLE WHERE isFavorite=1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int?): MovieEntity?

    @Update
    suspend fun update(movie: MovieEntity)

    @Query("SELECT id FROM MOVIE_TABLE WHERE isFavorite=1")
    fun getFavoriteMovieIds(): Flow<List<Int>>
}
