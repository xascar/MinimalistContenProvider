package com.example.minimalistcontentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import java.lang.Integer.parseInt


private const val TAG = "MiniContentProvider"

class MiniContentProvider: ContentProvider() {
    private lateinit var mData: Array<String>

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)


    override fun onCreate(): Boolean {
        val context = context
        mData = context?.resources?.getStringArray(R.array.words) as Array<String>
        initializeUriMatching()
        return true
    }


//    @Nullable
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var id = -1
        when (sUriMatcher.match(uri)) {
            0 -> {
                // Matches URI to get all of the entries.
                id = Contract.ALL_ITEMS
                // Look at the remaining arguments
                // to see whether there are constraints.
                // In this example, we only support getting
                //a specific entry by id. Not full search.
                // For a real-life app, you need error-catching code;
                // here we assume that the
                // value we need is actually in selectionArgs and valid.
                if (selection != null) {
                    selectionArgs?.let {
                        id = selectionArgs[0].toInt()
                    }

                }
            }
            1 ->
                // The URI ends in a numeric value, which represents an id.
                // Parse the URI to extract the value of the last,
                // numeric part of the path,
                // and set the id to that value.
                uri.lastPathSegment?.let {
                    id = parseInt(it)
                }

            UriMatcher.NO_MATCH -> {
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME.")
                id = -1
            }
            else -> {
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED.")
                id = -1
            }
        }
        Log.d(TAG, "query: $id")
        return populateCursor(id)
    }

    private fun populateCursor(id: Int): Cursor? {
        val cursor = MatrixCursor(arrayOf(Contract.CONTENT_PATH))
        // If there is a valid query, execute it and add the result to the cursor.
        if (id == Contract.ALL_ITEMS) {
            for (element in mData) {
                cursor.addRow(arrayOf<Any>(element))
            }
        } else if (id >= 0) {
            // Execute the query to get the requested word.
            val word = mData[id]
            // Add the result to the cursor.
            cursor.addRow(arrayOf<Any>(word))
        }
        return cursor
    }



    override fun getType(uri: Uri): String {
        return when (sUriMatcher.match(uri)) {
            0 -> Contract.MULTIPLE_RECORD_MIME_TYPE
            1 -> Contract.SINGLE_RECORD_MIME_TYPE
            else ->  throw Exception("Not implemented type")              // Alternatively, throw an exception.
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri {
        Log.e(TAG, "Not implemented: update uri: $p0")
        return p0
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
    private fun initializeUriMatching() {
        // # for returning a specific item
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", 1)
        // 0 for returning all items
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, 0)

    }

}