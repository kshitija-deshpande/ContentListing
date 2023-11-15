package com.diagnal.contentlisting

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 * For ContentListingViewModel
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ContentListingViewModelTest {


    @Test
    fun testContentListingFetch() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val viewModel = ContentListingViewModel(context = appContext)
        val content = viewModel.fetchContentList(1)
        assert(content != null)
    }

}