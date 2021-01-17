/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * DAO for our offline cache database
 * */

@Dao
interface VideoDao {

    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo) //vararg is how a function can take an unknown number of arguments in kotlin
}

/**
 * Create the VideosDatabase class
 * **/
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    abstract val videoDao: VideoDao

}

// use the singleton pattern to get an instance of the database
private lateinit var INSTANCE: VideosDatabase

// Define a getDatabase() function to return the VideosDatabase
fun getDatabase(context: Context): VideosDatabase {

    // thread safe
    synchronized(VideosDatabase::class.java) {
        //  check if the variable has been initialized
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }//end if 
    }
    return INSTANCE
}