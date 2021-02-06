package io.github.bgavyus.lightningcamera.graphics

import android.util.Size
import android.view.Surface
import io.github.bgavyus.lightningcamera.common.DeferScope
import io.github.bgavyus.lightningcamera.common.SingleThreadHandler
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class SurfaceDuplicatorFactory : DeferScope() {
    private val handler = SingleThreadHandler(javaClass.simpleName)
        .apply { defer(::close) }

    suspend fun create(
        bufferSize: Size,
        surfaces: Iterable<Surface>,
    ) = withContext(handler.asCoroutineDispatcher()) {
        SurfaceDuplicator(handler, bufferSize, surfaces)
    }
}
