package com.diagnal.contentlisting

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.diagnal.contentlisting.data.Content
import com.diagnal.contentlisting.data.Response
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * ViewModel class for the ContentListingActivity
 */
class ContentListingViewModel(
    private val context: Context
): ViewModel() {

    /**
     * Fetches the content list from the assets folder which has json response files
     */
    fun fetchContentList(pageNumber: Int): ArrayList<Content>? {
        Log.i(TAG, "fetchContentList: Fetching content list for page number $pageNumber.")

        val content = readJSONFromAssets(context, "CONTENTLISTINGPAGE-PAGE$pageNumber.json")
        val response = Gson().fromJson(content, Response::class.java)

        return response.page?.contentItems?.contents
    }

    /**
     * Reads the json file from the assets folder
     */
    private fun readJSONFromAssets(context: Context, path: String): String {
        Log.i(TAG, "readJSONFromAssets: Reading JSON from assets with path $path.")
        try {
            val file = context.assets.open("$path")
            Log.i(TAG, "ReadJSONFromAssets: Found File: $file.")
            val bufferedReader = BufferedReader(InputStreamReader(file))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            Log.i(TAG, "ReadJSONFromAssets: stringBuilder: $stringBuilder.")
            val jsonString = stringBuilder.toString()
            Log.i(TAG, "ReadJSONFromAssets: JSON as String: $jsonString.")
            return jsonString
        } catch (e: Exception) {
            Log.e(TAG, "ReadJSONFromAssets: Error reading JSON: $e.")
            e.printStackTrace()
            return ""
        }
    }

    companion object {
        private const val TAG = "ContentListingViewModel"
    }
}