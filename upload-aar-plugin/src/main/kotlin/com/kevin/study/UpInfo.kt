package com.kevin.study

import org.gradle.api.Project

open class UpInfo {

    var groupId: String? = null // 打出来的包的前缀
    var artifactId: String? = null // 打出来的包的名称
    var verCode: String? = null // 版本

    companion object {
        fun getUpInfo(project: Project) :UpInfo {
            val info = project.extensions.findByType(UpInfo::class.java)
            return info?: UpInfo()
        }
    }

    fun isValid() :Boolean {
        return !groupId.isNullOrEmpty() && !artifactId.isNullOrEmpty() && !verCode.isNullOrEmpty()
    }

    override fun toString(): String {
        return "groupId:$groupId | artifactId:$artifactId | verCode:$verCode"
    }

}