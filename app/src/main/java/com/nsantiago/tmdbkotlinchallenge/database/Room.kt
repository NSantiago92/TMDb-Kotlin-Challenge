package com.nsantiago.tmdbkotlinchallenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM databasemovie WHERE id=:id")
    fun getMovie(id: Int): DatabaseMovie?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie( movie:DatabaseMovie )
}

@Database(entities = [DatabaseMovie::class], version = 3)
abstract class MoviesDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}

fun getDatabase(context: Context): MoviesDatabase {
    return Room
        .databaseBuilder(context.applicationContext, MoviesDatabase::class.java,"movies")
        .fallbackToDestructiveMigration()
        .build()
}