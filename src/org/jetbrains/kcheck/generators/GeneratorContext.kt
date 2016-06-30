package org.jetbrains.kcheck.generators

import org.jetbrains.kcheck.Generator
import java.util.*
import javax.annotation.Generated

/**
 * Created by atsky on 14/07/15.
 */
class GeneratorContext(val random: Random) {
    fun nextInt(max : Int) : Int = random.nextInt(max)

    fun <T> generateFrom(g : Generator<T>) = g.generate(random)
 }