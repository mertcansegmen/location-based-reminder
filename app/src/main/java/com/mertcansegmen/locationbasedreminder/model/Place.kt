package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mertcansegmen.locationbasedreminder.R
import com.mertcansegmen.locationbasedreminder.ui.addeditreminder.Selectable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Place(
        @PrimaryKey(autoGenerate = true)
        val placeId: Long? = null,
        var name: String? = null,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var radius: Int = 0
) : Parcelable, Selectable {

    constructor(name: String, latitude: Double, longitude: Double, radius: Int) :
            this(null, name, latitude, longitude, radius)

    override fun getId(): Long {
        return placeId!!
    }

    override fun getDisplayText(): String? {
        return name
    }

    override fun getDisplayIcon(): Int {
        return R.drawable.ic_places
    }

    override fun getType(): Int {
        return Selectable.PLACE
    }
}