package com.kevin.study

import java.io.File

interface FileMerger {

    fun doMerge(inputFiles: Array<File>?, mergeOutDir: String?)

}