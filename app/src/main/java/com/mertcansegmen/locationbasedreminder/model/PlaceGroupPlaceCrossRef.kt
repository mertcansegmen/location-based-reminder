package com.mertcansegmen.locationbasedreminder.model

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["placeId", "placeGroupId"], indices = [Index("placeGroupId")])
data class PlaceGroupPlaceCrossRef(var placeId: Long, var placeGroupId: Long)