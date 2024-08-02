package com.example.projectmobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projectmobile.utilis.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

@Database(
    entities = [User::class, Activity::class,Favorite::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao
    abstract fun favoriteDao(): FavoriteDao

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9 )
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

// Migrazione dalla versione 4 alla versione 5
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Aggiungi la colonna 'latitude'
        db.execSQL("ALTER TABLE activities ADD COLUMN latitude REAL NOT NULL DEFAULT 0.0")

        // Aggiungi la colonna 'longitude'
        db.execSQL("ALTER TABLE activities ADD COLUMN longitude REAL NOT NULL DEFAULT 0.0")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Aggiungi la colonna 'category' alla tabella 'activities'
        db.execSQL("ALTER TABLE activities ADD COLUMN category TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Aggiungi la colonna 'feedback' come una stringa JSON
        db.execSQL("ALTER TABLE activities ADD COLUMN feedback TEXT NOT NULL DEFAULT ''")

        // Aggiungi la colonna 'phoneNumber'
        db.execSQL("ALTER TABLE activities ADD COLUMN phoneNumber TEXT NOT NULL DEFAULT ''")
    }
}

// Migrazione dalla versione 7 alla versione 8
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Aggiorna la colonna 'feedback' per avere una lista JSON vuota come valore predefinito
        db.execSQL("UPDATE activities SET feedback = '[]' WHERE feedback = ''")
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Rimuovi le tabelle Cart e Booking se esistono
        db.execSQL("DROP TABLE IF EXISTS Cart")
        db.execSQL("DROP TABLE IF EXISTS Booking")
    }
}
