package com.example.minimalistcontentprovider

import android.net.Uri

class Contract {
    companion object{

        val AUTHORITY = "com.android.example.minimalistcontentprovider.provider"
        val CONTENT_PATH = "words"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")
        val ALL_ITEMS = -2
        val WORD_ID = "id"
        val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.words"
        val MULTIPLE_RECORD_MIME_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.words"
    }


}