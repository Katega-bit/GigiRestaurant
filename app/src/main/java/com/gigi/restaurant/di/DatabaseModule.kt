package com.gigi.restaurant.di

import android.content.Context
import androidx.room.Room
import com.gigi.restaurant.data.local.AppDatabase
import com.gigi.restaurant.data.local.RestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "gigi_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRestaurantDao(db: AppDatabase): RestaurantDao =
        db.restaurantDao()
}
