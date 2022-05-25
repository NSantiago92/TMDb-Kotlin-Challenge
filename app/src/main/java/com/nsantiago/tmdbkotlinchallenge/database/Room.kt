package com.nsantiago.tmdbkotlinchallenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM databasemovie WHERE id=:id")
    fun getMovie(id: Int): LiveData<DatabaseMovie>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie( movie:DatabaseMovie )
}

@Dao
interface GenreDao {
    //TODO: add queries get all and insert all
}
@Database(entities = [DatabaseMovie::class, DatabaseGenre::class], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java,
                "movies").build()
        }
    }
    return INSTANCE
}