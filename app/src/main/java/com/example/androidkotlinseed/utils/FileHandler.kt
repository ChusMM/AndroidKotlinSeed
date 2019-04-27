package com.example.androidkotlinseed.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class FileHandler : Handler {
    private val TAG = FileHandler::class.java.simpleName

    companion object {
        const val WRITE_OK = 0
        const val WRITE_FAILED = 1

        const val READ_OK = 2
        const val READ_FAILED = 3

        const val FILE_PATH_KEY = "file_path_key"
        const val FILE_CONTENT_KEY = "file_content_key"
        const val THROWABLE_KEY = "throwable_key"
    }

    interface WriteListener {
        fun writeFinish(filePath: String)
        fun writeFailed(throwable: Throwable)
    }

    interface ReadListener {
        fun readFinish(content: String)
        fun readFailed(throwable: Throwable)
    }

    var writeListener: WriteListener? = null
    var readListener: ReadListener? = null

    constructor(writeListener: WriteListener) : super(Looper.getMainLooper()) {
        this.writeListener = writeListener
    }

    constructor(readListener: ReadListener) : super(Looper.getMainLooper()) {
        this.readListener = readListener
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val throwable: Throwable

        when (msg.what) {
            WRITE_OK -> {
                val path = msg.data.getString(FILE_PATH_KEY) ?: ""
                writeListener?.writeFinish(path)
            }
            WRITE_FAILED -> {
                throwable = msg.data.getSerializable(THROWABLE_KEY) as Throwable
                writeListener?.writeFailed(throwable)
            }
            READ_OK -> {
                val content = msg.data.getString(FILE_CONTENT_KEY) ?: ""
                readListener?.readFinish(content)
            }
            READ_FAILED -> {
                throwable = msg.data.getSerializable(THROWABLE_KEY) as Throwable
                readListener?.readFailed(throwable)
            }
            else -> Log.e(TAG, "Message not handled")
        }
    }
}
