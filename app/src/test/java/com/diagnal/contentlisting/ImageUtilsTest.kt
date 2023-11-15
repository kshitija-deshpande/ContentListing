package com.diagnal.contentlisting

import com.diagnal.contentlisting.utils.ImageUtils
import org.junit.Test

class ImageUtilsTest {
    @Test
    fun correctPosterTest() {
        val resId = ImageUtils().getImageResource("poster1.jpg")
        assert(resId == R.drawable.poster1)
    }

    @Test
    fun incorrectPosterTest() {
        val resId = ImageUtils().getImageResource("poster99.jpg")
        assert(resId == R.drawable.placeholder_for_missing_posters)
    }
}