package com.example.minimalistcontentprovider

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.minimalistcontentprovider.databinding.ActivityMainBinding


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            buttonDisplayAll.setOnClickListener { onClickDisplayEntries(it) }
            buttonDisplayFirst.setOnClickListener { onClickDisplayEntries(it) }
        }



        setContentView(binding.root)
    }

    fun onClickDisplayEntries(view: View) {
        //region Testing click
//        Log.d(TAG, "Yay, I was clicked!")
//
//        binding.apply {
//            when (view.id) {
//                buttonDisplayAll.id -> Log.d(
//                    TAG,
//                    "Yay, " + buttonDisplayAll.text.toString() + " was clicked!"
//                )
//                buttonDisplayFirst.id -> Log.d(
//                    TAG,
//                    "Yay, " + buttonDisplayFirst.text.toString() + " was clicked!"
//                )
//                else -> Log.d(TAG, "Error. This should never happen.")
//            }
//            textview.append("Thus we go! \n")
//        }
//endregion

        val queryUri = Contract.CONTENT_URI.toString()
        // Only get words
        val projection = arrayOf(Contract.CONTENT_PATH)

        var selectionClause: String?
        var selectionArgs: Array<String>?

        when (view.id) {
            binding.buttonDisplayAll.id -> {
                selectionClause = null
                selectionArgs = null
            }
            binding.buttonDisplayFirst.id -> {
                selectionClause = Contract.WORD_ID + " = ?"
                selectionArgs = arrayOf("0")
            }
            else -> {
                selectionClause = null
                selectionArgs = null
            }
        }

        val sortOrder = ""


        val cursor: Cursor? = contentResolver.query(
            Uri.parse(queryUri),
            projection,
            selectionClause,
            selectionArgs,
            sortOrder
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(projection[0])
                do {
                    val word = cursor.getString(columnIndex)
                    binding.textview.append(
                        """
                    $word
                    
                    """.trimIndent()
                    )
                } while (cursor.moveToNext())
            } else {
                Log.d(TAG, "onClickDisplayEntries " + "No data returned.")
                binding.textview.append(
                    """
                No data returned.
                
                """.trimIndent()
                )
            }
            cursor.close()
        } else {
            Log.d(TAG, "onClickDisplayEntries " + "Cursor is null.")
            binding.textview.append(
                """
            Cursor is null.
            
            """.trimIndent()
            )
        }


    }

}