package io.github.bgavyus.lightningcamera.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RotationTest {
    @Test
    fun isLandscape() {
        assertFalse(Rotation.Natural.isLandscape)
        assertTrue(Rotation.Right.isLandscape)
        assertFalse(Rotation.UpsideDown.isLandscape)
        assertTrue(Rotation.Left.isLandscape)
    }

    @Test
    fun minus() {
        assertEquals(Rotation.UpsideDown, Rotation.Left - Rotation.Right)
        assertEquals(Rotation.Left, Rotation.Natural - Rotation.Right)
        assertEquals(Rotation.Natural, Rotation.UpsideDown - Rotation.UpsideDown)
        assertEquals(Rotation.UpsideDown, Rotation.UpsideDown - Rotation.Natural)
    }
}
