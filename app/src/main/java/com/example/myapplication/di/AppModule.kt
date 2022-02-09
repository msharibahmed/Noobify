package com.example.myapplication.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module // module annotation
@InstallIn(SingletonComponent::class) // restricting lifetime of AppModule instance to our Application's lifetime
object AppModule {


}