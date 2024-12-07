package com.example.wesafe_humansafety

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class MyFamilyDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao



    companion object {

        @Volatile
        private var INSTANCE: MyFamilyDatabase? = null

        fun getDatabase(context: Context): MyFamilyDatabase {


            return INSTANCE?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyFamilyDatabase::class.java,
                    "my_family_db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}