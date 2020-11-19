package com.user.superhero.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hero(
    val response: String,
    val id: String,
    val name: String,
    val powerstats: APIResponse.Powerstats,
    val biography: APIResponse.Biography,
    val appearance: APIResponse.Appearance,
    val work: APIResponse.Work,
    val connections: APIResponse.Connections,
    val image: APIResponse.Image,
    var isSuggestion: Boolean
) : Parcelable