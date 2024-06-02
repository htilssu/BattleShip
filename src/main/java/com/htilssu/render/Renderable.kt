package com.htilssu.render

import java.awt.Graphics

interface Renderable {
    fun render(g: Graphics): Unit
}