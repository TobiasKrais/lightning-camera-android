package io.github.bgavyus.lightningcamera.extensions.android.media

import android.media.MediaCodec
import android.media.MediaCrypto
import android.media.MediaFormat
import android.os.Handler
import io.github.bgavyus.lightningcamera.extensions.kotlinx.coroutines.cancel
import io.github.bgavyus.lightningcamera.utilities.OptionSet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

fun MediaCodec.encoderEvents(handler: Handler? = null) = callbackFlow {
    val callback = object : MediaCodec.Callback() {
        val bufferAvailableEvents = HashMap<Int, EncoderEvent.BufferAvailable>()

        override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
            trySendBlocking(EncoderEvent.FormatChanged(format))
        }

        override fun onOutputBufferAvailable(
            codec: MediaCodec,
            index: Int,
            info: MediaCodec.BufferInfo,
        ) {
            val event = bufferAvailableEvents
                .getOrPut(index) { EncoderEvent.BufferAvailable(index, info) }
                .also { it.info.copyFrom(info) }

            trySendBlocking(event)
        }

        override fun onError(codec: MediaCodec, error: MediaCodec.CodecException) = cancel(error)
        override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {}
    }

    setCallback(callback, handler)
    awaitClose()
}

sealed class EncoderEvent {
    data class FormatChanged(val format: MediaFormat) : EncoderEvent()
    data class BufferAvailable(val index: Int, val info: MediaCodec.BufferInfo) : EncoderEvent()
}

fun MediaCodec.configureEncoder(format: MediaFormat? = null, crypto: MediaCrypto? = null) =
    configure(format, null, crypto, MediaCodec.CONFIGURE_FLAG_ENCODE)

fun MediaCodec.tryStop() {
    runCatching { stop() }
}

fun MediaCodec.tryFlush() {
    runCatching { flush() }
}

val MediaCodec.BufferInfo.flagsSet get() = OptionSet(flags)

fun MediaCodec.BufferInfo.copyFrom(other: MediaCodec.BufferInfo) =
    set(other.offset, other.size, other.presentationTimeUs, other.flags)
