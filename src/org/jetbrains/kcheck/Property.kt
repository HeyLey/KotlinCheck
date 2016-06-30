package org.jetbrains.kcheck

import com.sun.tools.internal.ws.wscompile.Options
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * @author
 * @since 7/14/15.
 */

Retention(RetentionPolicy.RUNTIME)
Target(ElementType.METHOD)
public annotation class property