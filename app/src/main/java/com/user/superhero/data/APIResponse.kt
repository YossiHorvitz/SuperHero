package com.user.superhero.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class APIResponse(
    val response: String,
    val results: List<Hero>
) : Parcelable {

    @Parcelize
    data class Powerstats(
        val intelligence: String,
        val strength: String,
        val speed: String,
        val durability: String,
        val power: String,
        val combat: String
    ) : Parcelable

    @Parcelize
    data class Biography(
        @SerializedName("full-name")
        val full_name: String,
        @SerializedName("alter-egos")
        val alter_egos: String,
        @SerializedName("place-of-birth")
        val place_of_birth: String,
        @SerializedName("first-appearance")
        val first_appearance: String,
        val publisher: String,
        val alignment: String,
        val aliases: List<String>
    ) : Parcelable

    @Parcelize
    data class Appearance(
        val gender: String,
        val race: String,
        val height: List<String>,
        val weight: List<String>,
        @SerializedName("eye-color")
        val eye_color: String,
        @SerializedName("hair-color")
        val hair_color: String
    ) : Parcelable

    @Parcelize
    data class Work(
        val occupation: String,
        val base: String
    ) : Parcelable

    @Parcelize
    data class Connections(
        @SerializedName("group-affiliation")
        val group_affiliation: String,
        val relatives: String
    ) : Parcelable

    @Parcelize
    data class Image(
        val url: String
    ) : Parcelable
}