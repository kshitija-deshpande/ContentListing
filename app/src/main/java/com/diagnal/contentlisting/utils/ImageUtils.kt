package com.diagnal.contentlisting.utils

import com.diagnal.contentlisting.R

class ImageUtils {

    fun getImageResource(imageId: String?): Int {

        imageId?.let {
            when(imageId) {
                "poster1.jpg" -> {
                    return R.drawable.poster1
                }
                "poster2.jpg" -> {
                    return R.drawable.poster2
                }
                "poster3.jpg" -> {
                    return R.drawable.poster3
                }
                "poster4.jpg" -> {
                    return R.drawable.poster4
                }
                "poster5.jpg" -> {
                    return R.drawable.poster5
                }
                "poster6.jpg" -> {
                    return R.drawable.poster6
                }
                "poster7.jpg" -> {
                    return R.drawable.poster7
                }
                "poster8.jpg" -> {
                    return R.drawable.poster8
                }
                "poster9.jpg" -> {
                    return R.drawable.poster9
                }
                else -> {
                    return R.drawable.placeholder_for_missing_posters
                }
            }
        }
        return R.drawable.placeholder_for_missing_posters
    }
}