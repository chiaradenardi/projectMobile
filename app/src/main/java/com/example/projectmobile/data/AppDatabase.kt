package com.example.projectmobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, Activity::class, Booking::class, Favorite::class, Cart::class],
    version = 4,  // Incrementa la versione del database a 4
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)  // Aggiungi tutte le migrazioni necessarie
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Migrazione dalla versione 1 alla versione 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE User ADD COLUMN darkMode INTEGER NOT NULL DEFAULT 0")
    }
}

// Migrazione dalla versione 2 alla versione 3
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE User ADD COLUMN showGamificationBadge INTEGER NOT NULL DEFAULT 1")
    }
}

// Migrazione dalla versione 3 alla versione 4
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Crea una nuova tabella con la struttura aggiornata
        db.execSQL("""
            CREATE TABLE User_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                bio TEXT,
                profileImage TEXT,
                darkMode INTEGER NOT NULL DEFAULT 0
            )
        """)

        // Copia i dati dalla tabella esistente alla nuova tabella
        db.execSQL("""
            INSERT INTO User_new (id, username, email, password, firstName, lastName,  bio, profileImage, darkMode)
            SELECT id, username, email, password, firstName, lastName,  bio, profileImage, darkMode
            FROM User
        """)

        // Rimuovi la tabella originale
        db.execSQL("DROP TABLE User")

        // Rinomina la nuova tabella per sostituire l'originale
        db.execSQL("ALTER TABLE User_new RENAME TO User")

        // Aggiungi l'indice univoco su username
        db.execSQL("CREATE UNIQUE INDEX index_User_username ON User (username)")
    }
}
