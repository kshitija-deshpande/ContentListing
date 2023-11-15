package com.diagnal.contentlisting.data

import com.google.gson.annotations.SerializedName

class Content {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("poster-image")
    var posterImage: String? = null
}