package com.diagnal.contentlisting

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 * For ContentListingViewModel
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testRecyclerViewIsPresent() {
        val mainActivity = MainActivity()
        mainActivity.onBackPressed()
        assertTrue(mainActivity.backButtonClicked)
    }
}