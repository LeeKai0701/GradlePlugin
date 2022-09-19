package com.kevin.study

import org.gradle.api.Action
import org.gradle.api.tasks.Input
import java.io.FileFilter

open class DefaultMergeHandler: MergeHandler {

    @Input
    var filter: FileFilter = DefaultFileFilter("_router.json")

    @Input
    var merger: FileMerger = DefaultJsonMerger("app_router.json")

    override fun getFileFilter(): FileFilter {
        return filter
    }

    override fun setFileFilter(filter: FileFilter) {
        this.filter = filter
    }

    override fun fileFilter(configure: Action<in FileFilter>) {
        configure.execute(filter)
    }

    override fun getFileMerger(): FileMerger {
        return merger
    }

    override fun setFileMerger(merger: FileMerger) {
        this.merger = merger
    }

    override fun fileMerger(configure: Action<in FileMerger>) {
        configure.execute(merger)
    }


}