package com.mertcansegmen.locationbasedreminder.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.mertcansegmen.locationbasedreminder.R
import com.mertcansegmen.locationbasedreminder.ui.addeditreminder.Selectable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceGroupWithPlaces(
        @Embedded var placeGroup: PlaceGroup? = null,
        @Relation(
                parentColumn = "placeGroupId",
                entityColumn = "placeId",
                associateBy = Junction(PlaceGroupPlaceCrossRef::class)
        )
        var places: List<Place>? = null
) : Parcelable, Selectable {

    override fun getId(): Long {
        return placeGroup?.placeGroupId!!
    }

    override fun getDisplayText(): String? {
        return placeGroup?.name
    }

    override fun getDisplayIcon(): Int {
        return R.drawable.ic_place_groups_small
    }

    override fun getType(): Int {
        return Selectable.PLACE_GROUP
    }
}