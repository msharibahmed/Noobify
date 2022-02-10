package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.helpers.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // module annotation
@InstallIn(SingletonComponent::class) // restricting lifetime of AppModule instance to our Application's lifetime
object AppModule {

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)
}