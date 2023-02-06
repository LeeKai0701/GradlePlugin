package com.kevin.study

import org.gradle.api.tasks.Input
import java.io.File
import java.io.FileFilter

open class DefaultFileFilter(suffix: String) : FileFilter {

    @Input
    var nameSuffix: String = suffix
        set(value) {
            println("DefaultFileFilter- suffix: $value")
            field = value
        }

    override fun accept(pathname: File?): Boolean {
        return pathname?.name?.endsWith(nameSuffix) == true
    }


}