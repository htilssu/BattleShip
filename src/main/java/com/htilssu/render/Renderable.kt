package com.htilssu.render

import java.awt.Graphics

fun interface Renderable {
    fun render(g: Graphics): Unit
}