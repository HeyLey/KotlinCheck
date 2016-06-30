package org.jetbrains.kcheck

import org.jetbrains.kcheck.generators.DoubleGenerator
import org.jetbrains.kcheck.generators.StringGenerator
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.manipulation.Filter
import org.junit.runner.manipulation.Filterable
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import java.lang.Double
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.jvm.kotlin


/**
 * @since 7/13/15.
 */
class KCheck(val klass : Class<*>) : Runner(), Filterable {
    val adescription = Description.createSuiteDescription(klass.getName()!!)
    var methods : List<Method>

    init {
        methods = klass.getDeclaredMethods().filter { isProperty(it) }.toArrayList()
    }


    fun isProperty(method : Method) : Boolean {
        val arrayOfAnnotations = method.getAnnotations()
        return arrayOfAnnotations.any { it.annotationType() == javaClass<property>() }
    }

    override fun getDescription(): Description {
        return adescription
    }

    override fun filter(filter: Filter) {
        methods = methods.filter { filter.shouldRun(methodDescription(it)) }.toArrayList()
    }

    protected fun testName(method: Method): String {
        return method.getName()
    }


    protected fun testAnnotations(method: Method): Array<Annotation> {
        return method.getAnnotations()
    }

    protected fun methodDescription(method: Method): Description {
        return Description.createTestDescription(klass, testName(method), *testAnnotations(method))
    }

    private val sampleSize = 1000

    override fun run(notifier: RunNotifier) {
        for (method in methods) {
            val description = methodDescription(method)
            notifier.fireTestStarted(description)

            val random = Random()

            for (i in 0..sampleSize) {
                val parameterTypes = method.getGenericParameterTypes()
                val args = Array(parameterTypes.size(), { index ->
                    generateParameter(parameterTypes[index], random)
                })

                val obj = klass.newInstance()
                val result = method.invoke(obj, *args)

                if (result != true) {
                    print("Test failed");
                    print(Arrays.toString(args))
                    throw RuntimeException()
                }
            }

            notifier.fireTestFinished(description)
        }
    }

    private fun generateParameter(type: Type, random : Random): Any {
        return when (type) {
            javaClass<String>() -> {
                StringGenerator().generate(random)
            }
            Double.TYPE -> {
                DoubleGenerator().generate(random)
            }
            else -> throw RuntimeException()
        }
    }

}
