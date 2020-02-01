package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Reminder(
        @PrimaryKey(autoGenerate = true)
        val reminderId: Long?,
        var noteId: Long,
        var placeId: Long?,
        var placeGroupId: Long?,
        var isActive: Boolean
) : Parcelable {
    constructor(noteId: Long, placeId: Long?, placeGroupId: Long?, isActive: Boolean) :
            this(null, noteId, placeId, placeGroupId, isActive)
}