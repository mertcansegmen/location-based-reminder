package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReminderWithNotePlacePlaceGroup(
        @Embedded var reminder: Reminder? = null,

        @Relation(parentColumn = "noteId", entityColumn = "noteId")
        var note: Note? = null,

        @Relation(parentColumn = "placeId", entityColumn = "placeId")
        var place: Place? = null,

        @Relation(parentColumn = "placeGroupId", entityColumn = "placeGroupId", entity = PlaceGroup::class)
        var placeGroupWithPlaces: PlaceGroupWithPlaces? = null
) : Parcelable