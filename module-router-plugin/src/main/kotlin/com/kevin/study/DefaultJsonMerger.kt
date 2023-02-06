package com.kevin.study

import org.gradle.api.tasks.Input
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * 默认的合成器只是支持把
 * {"key1":1} 和 {"key2":"2"} 合并为 {"key1":1,"key2":"2"}
 * 如果key相同，则会覆盖
 */
open class DefaultJsonMerger(fileName: String): FileMerger {

    @Input
    var mergedName: String = fileName
        set(value) {
            println("DefaultJsonMerger- fileName: $value")
            field = value
        }

    @Input
    var deleteOrigins: Boolean = true

    @Input
    var isJsonArray: Boolean = false

    override fun doMerge(inputFiles: Array<File>?, mergeOutDir: String?) {
        if (inputFiles.isNullOrEmpty() || mergeOutDir.isNullOrEmpty()) {
            println("DefaultJsonMerger- no input or output invalid")
            return
        }

        if (inputFiles != null) {
            for (i in inputFiles.indices) {
                println("Kevin-- mergeIn" + i + ":" + inputFiles[i].name)
            }
        }

        println("Kevin-- mergeOut:" + mergeOutDir)

        if (isJsonArray) {
            mergeJsonArray(inputFiles, mergeOutDir)
        } else {
            mergeJsonObject(inputFiles, mergeOutDir)
        }

    }

    private fun mergeJsonObject(inputFiles: Array<File>, mergeOutDir: String?) {
        var jsonMap:Map<String, Any>
        val newJsonObj = JSONObject()
        inputFiles.forEach { file ->
            val fileStr = file.readText()
            try {
                jsonMap = JSONObject(fileStr).toMap()
                for (entry in jsonMap) {
                    newJsonObj.putOpt(entry.key, entry.value)
                }
            } catch (e: Exception) {
                // do nothing
            }
            if (deleteOrigins) {
                file.delete()
            }
        }

        val newFile = File(mergeOutDir, mergedName)
        newFile.writeText(newJsonObj.toString())
    }

    private fun mergeJsonArray(inputFiles: Array<File>, mergeOutDir: String?) {
        var jsonArray:List<Any?>
        val newJsonArray = JSONArray()
        inputFiles.forEach { file ->
            val fileStr = file.readText()
            try {
                jsonArray = JSONArray(fileStr).toList()
                for (ele in jsonArray) {
                    if (ele != null) {
                        newJsonArray.put(ele)
                    }
                }
            } catch (e: Exception) {
                // do nothing
            }
            if (deleteOrigins) {
                file.delete()
            }
        }

        val newFile = File(mergeOutDir, mergedName)
        newFile.writeText(newJsonArray.toString())
    }

}