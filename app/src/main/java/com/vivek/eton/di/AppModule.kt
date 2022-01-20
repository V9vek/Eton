package com.vivek.eton.di

import android.app.Application
import androidx.room.Room
import com.vivek.eton.feature_note.data.data_source.NoteDatabase
import com.vivek.eton.feature_note.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import com.vivek.eton.feature_note.data.repository.NoteRepositoryImpl
import com.vivek.eton.feature_note.domain.repository.NoteRepository
import com.vivek.eton.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(dao = db.noteDao)
    }

    @Singleton
    @Provides
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository = repository),
            deleteNote = DeleteNote(repository = repository),
            addNote = AddNote(repository = repository),
            getNote = GetNote(repository = repository)
        )
    }
}




















