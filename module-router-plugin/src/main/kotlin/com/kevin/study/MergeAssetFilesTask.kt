package com.kevin.study

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class MergeAssetFilesTask// Use the factory ...
@Inject constructor(workPath: String?, handler: MergeHandler?) : DefaultTask() {

    @Input
    var dirPath: String? = workPath

    @Input
    var mergeHandler: MergeHandler? = handler

    override fun getGroup(): String? {
        return "kevin"
    }

    @TaskAction
    fun mergeAssetsWithHandler() {
        if (dirPath.isNullOrEmpty()) {
            println("MergeAssetFilesTask- merge assets output path is null or empty")
            return
        }

        if (mergeHandler == null) {
            println("MergeAssetFilesTask- mergeHandler is null")
            return
        }

        val dirFile = File(dirPath!!)
        if (!dirFile.exists()) {
            println("MergeAssetFilesTask- merge assets output dir not exists: $dirPath")
            return
        }

        val files = dirFile.listFiles(mergeHandler?.getFileFilter())
        mergeHandler?.getFileMerger()?.doMerge(files, dirPath)

    }

}