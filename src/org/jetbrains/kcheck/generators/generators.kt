package org.jetbrains.kcheck.generators

import org.jetbrains.kcheck.Generator
import java.util.*


/**
 * @since 7/14/15.
 */

object asciiCharGenerator : Generator<Char> {
    override fun generate(random: Random): Char =
        (random.nextInt(128 - 32) + 32).toChar()

}

fun <T> makeGenerator(body : GeneratorContext.() -> T) : Generator<T> = object : Generator<T> {
    override fun generate(random: Random): T = GeneratorContext(random).body()
}

inline fun <reified T> arrayGenerator(elementGenerator : Generator<T>) : Generator<Array<T>> = makeGenerator {
    Array<T>(nextInt(1000)) {
        generateFrom(elementGenerator)
    }
}

class StringGenerator(val charGenerator : Generator<Char> = asciiCharGenerator) : Generator<String> {
    override fun generate(random: Random): String = String(arrayGenerator(charGenerator).generate(random).toCharArray())
}

class DoubleGenerator() : Generator<Double> {
    override fun generate(random: Random) : Double {
        return random.nextDouble()
    }
}