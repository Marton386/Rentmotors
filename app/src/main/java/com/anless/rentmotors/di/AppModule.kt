package com.anless.rentmotors.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import android.content.Context
import com.anless.rentmotors.api.*
import com.anless.rentmotors.repositories.*
import dagger.hilt.components.SingletonComponent
import com.anless.rentmotors.helpers.SettingsHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import com.anless.rentmotors.models.converters.CarConverter
import com.anless.rentmotors.models.converters.OrgConverter
import com.anless.rentmotors.models.converters.StationConverter

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettingsHelper(@ApplicationContext context: Context) = SettingsHelper(context)

    @Provides
    @Singleton
    fun provideStationConverter() = StationConverter()

    @Provides
    @Singleton
    fun provideOrgConverter() = OrgConverter()

    @Provides
    @Singleton
    fun provideStationsRepository(stationConverter: StationConverter): StationRepository {
        val stationAPI = ServiceGenerator.createService(StationAPI::class.java)
        return StationRepository(stationAPI, stationConverter)
    }

    @Provides
    @Singleton
    fun provideOrgRepository(orgConverter: OrgConverter): OrgRepository {
        val orgAPI = ServiceOrgGenerator.createService(OrgAPI::class.java)
        return OrgRepository(orgAPI, orgConverter)
    }

    @Provides
    @Singleton
    fun provideCarsConverter() = CarConverter()

    @Provides
    @Singleton
    fun provideCarRepository(carConverter: CarConverter): CarRepository {
        val carsAPI = ServiceGenerator.createService(CarAPI::class.java)
        return CarRepository(carsAPI, carConverter)
    }

    @Provides
    @Singleton
    fun provideBookRepository(): BookRepository {
        val bookAPI = ServiceGenerator.createService(BookAPI::class.java)
        return BookRepository(bookAPI)
    }


    @Provides
    @Singleton
    fun provideVoucherRepository(@ApplicationContext context: Context): VoucherRepository {
        val voucherAPI = ServiceGenerator.createService(VoucherAPI::class.java)
        return VoucherRepository(voucherAPI, context.cacheDir)
    }

    @Provides
    @Singleton
    fun provideCheckClientRepository(): CheckClientRepository {
        val checkClientAPI = ServiceGenerator.createService(CheckClientAPI::class.java)
        return CheckClientRepository(checkClientAPI)
    }
}