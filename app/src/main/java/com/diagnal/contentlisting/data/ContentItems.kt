package com.diagnal.contentlisting.data

import com.google.gson.annotations.SerializedName

class ContentItems {

    @SerializedName("content")
    var contents: ArrayList<Content>? = null
}