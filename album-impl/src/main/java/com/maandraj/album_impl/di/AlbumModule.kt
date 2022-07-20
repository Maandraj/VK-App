package com.maandraj.album_impl.di

import android.content.Context
import com.maandraj.album_impl.data.mapper.PhotosMapper
import com.maandraj.album_impl.data.repository.album.AlbumRepo
import com.maandraj.album_impl.data.repository.album.AlbumRepoImpl
import com.maandraj.album_impl.domain.interactor.AlbumInteractor
import com.maandraj.album_impl.ui.album.AlbumScreenVM
import dagger.Module
import dagger.Provides

@Module
class AlbumModule {
    @AlbumScreenScope
    @Provides
    fun provideAlbumScreenVM(albumInteractor: AlbumInteractor): AlbumScreenVM =
        AlbumScreenVM(albumInteractor = albumInteractor)

    @AlbumScreenScope
    @Provides
    fun provideAlbumRepo(context: Context, photosMapper: PhotosMapper): AlbumRepo =
        AlbumRepoImpl(context = context, photosMapper = photosMapper)
}