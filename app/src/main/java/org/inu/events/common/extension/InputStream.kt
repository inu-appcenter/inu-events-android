package org.inu.events.common.extension

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.InputStream

fun InputStream.asRequestBody(contentType: MediaType? = null): RequestBody {
    val byteArray = this.readBytes()

    return object : RequestBody() {
        override fun contentType() = contentType

        override fun contentLength() = byteArray.size.toLong()

        override fun writeTo(sink: BufferedSink) {
            sink.write(byteArray)
        }
    }
}