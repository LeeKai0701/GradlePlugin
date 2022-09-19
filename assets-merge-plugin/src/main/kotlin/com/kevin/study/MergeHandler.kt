package com.kevin.study

import org.gradle.api.Action
import java.io.FileFilter

interface MergeHandler {

    fun getFileFilter(): FileFilter

    fun setFileFilter(filter: FileFilter)

    fun fileFilter(configure: Action<in FileFilter>)

    fun getFileMerger(): FileMerger

    fun setFileMerger(merger: FileMerger)

    fun fileMerger(configure: Action<in FileMerger>)

}