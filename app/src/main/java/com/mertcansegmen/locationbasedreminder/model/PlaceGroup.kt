package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class PlaceGroup(@PrimaryKey(autoGenerate = true) val placeGroupId: Long?, var name: String) : Parcelable {
    constructor(name: String) : this(null, name)
}