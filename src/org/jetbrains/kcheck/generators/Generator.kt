package org.jetbrains.kcheck

import java.util.*

interface Generator<T> {
    fun generate(random : Random) : T
}