package com.example.bromoindah.di

import com.example.bromoindah.data.repository.AuthRepositoryImpl
import com.example.bromoindah.data.repository.BookingRepositoryImpl
import com.example.bromoindah.data.repository.ReviewRepositoryImpl
import com.example.bromoindah.data.repository.WisataRepositoryImpl
import com.example.bromoindah.domain.repository.AuthRepository
import com.example.bromoindah.domain.repository.BookingRepository
import com.example.bromoindah.domain.repository.ReviewRepository
import com.example.bromoindah.domain.repository.WisataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWisataRepository(
        wisataRepositoryImpl: WisataRepositoryImpl
    ): WisataRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}
