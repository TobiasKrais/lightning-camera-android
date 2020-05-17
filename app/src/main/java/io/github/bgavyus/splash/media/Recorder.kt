package io.github.bgavyus.splash.media

import android.view.Surface

interface Recorder : AutoCloseable {
    val surface: Surface
    fun record()
    fun loss()
}
