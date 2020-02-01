package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Note(
        @PrimaryKey(autoGenerate = true)
        val noteId: Long?,
        var title: String,
        var body: String
) : Parcelable {
    @Ignore
    constructor(body: String) : this(null, "", body)

    constructor(title: String, body: String) : this(null, title, body)
}