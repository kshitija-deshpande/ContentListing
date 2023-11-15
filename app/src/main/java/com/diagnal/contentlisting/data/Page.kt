package com.diagnal.contentlisting.data

import com.google.gson.annotations.SerializedName

class Page {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("total-content-items")
    var totalContentItems: String? = null

    @SerializedName("page-num")
    var pageNum: String? = null

    @SerializedName("page-size")
    var pageSize: String? = null

    @SerializedName("content-items")
    var contentItems: ContentItems? = null
}