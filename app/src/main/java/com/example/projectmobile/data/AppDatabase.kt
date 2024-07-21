package com.example.projectmobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, Activity::class, Booking::class, Favorite::class, Cart::class],
    version = 2,  // Incrementa la versione del database
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao
    abstract fun bookingDao(): BookingDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao

    companion object {
        private const val DATABASE_NAME = "project_mobile_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)  // Aggiungi migrazioni se necessario
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Migrazione corretta per aggiungere solo la colonna darkMode
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE User ADD COLUMN darkMode INTEGER NOT NULL DEFAULT 0")
    }
}
