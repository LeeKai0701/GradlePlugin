package com.kevin.study

class AarUploadPlugin: Plugin<Project> {

    companion object {
        const val UPLOAD_INFO = "upInfo"
    }

    override fun apply(project: Project) {
        // 0、引入上传组件
        project.plugins.apply("maven-publish")
        // 1、定义扩展参数
        project.extensions.create(UPLOAD_INFO, UpInfo::class.java)
        // 2、同步配置完成后，决定打完包之后执行上传操作
        project.afterEvaluate {
            val info = UpInfo.getUpInfo(project)
            if (!info.isValid()) {
                return@afterEvaluate
            }
            // 3、配置上传信息，Android官网提示仓库配置需要在afterEvaluate执行
            val isSnapshot = info.version?.endsWith("-SNAPSHOT") ?: false
            val isLocal = info.version?.endsWith("-LOCAL") ?: false
            configureProjectPlugin(project, isSnapshot, isLocal)
            // 4、建立上传的任务依赖关系
            val uploadReleaseTask = generateReleaseUploadTask(project, info)

            val publishTask: Task? = when {
                isLocal -> {
                    project.tasks.findByName("publishReleasePublicationToMavenLocal")
                }
                isSnapshot -> {
                    project.tasks.findByName("publishReleasePublicationToSnapshotRepository")
                }
                else -> {
                    project.tasks.findByName("publishReleasePublicationToReleaseRepository")
                }
            }

            uploadReleaseTask.dependsOn(publishTask)
        }
    }

    private fun generateReleaseUploadTask(project: Project, upInfo: UpInfo): Task {
        return project.tasks.create("uploadRelease") { task ->
            task.group = "kevin"
            task.doLast {
                println("")
                println("")
                println("implementation '${upInfo.groupId}:${upInfo.artifactId}:${upInfo.version}'")
                println("")
                println("")
            }
        }
    }

    private fun configureProjectPlugin(project: Project, isSnapshot: Boolean, isLocal: Boolean) {
        // 这里开始配置publishing中的内容
        project.extensions.configure(PublishingExtension::class.java) {
            configurePublications(project, it)
            configureRepositories(isSnapshot, isLocal, it)
        }
    }

    private fun configurePublications(project: Project, publishing: PublishingExtension) {
        val libExtension = (project.extensions.findByName("android") as LibraryExtension)
        val sourceTask = project.tasks.create("upSourcesJar", Jar::class.java) { task ->
            task.archiveClassifier.set("sources")
            task.from(libExtension.sourceSets.findByName("main")?.java?.srcDirs)
        }
        libExtension.libraryVariants.all {
            println("Kevin-- ${it.mergeAssetsProvider.get().outputDir.asFile.get().absolutePath}")
        }

        publishing.publications { container ->
            container.create("release", MavenPublication::class.java) { maven ->
                val info = UpInfo.getUpInfo(project)
                maven.groupId = info.groupId
                maven.artifactId = info.artifactId
                maven.version = info.version
                maven.from(project.components.findByName("release"))
                maven.artifact(sourceTask)
            }
        }
    }

    private fun configureRepositories(isSnapshot: Boolean, isLocal: Boolean, publishing: PublishingExtension) {
        publishing.repositories { handler ->
            if (isLocal) {
                return@repositories
            }
            handler.maven { repo ->
                repo.name = if (isSnapshot) { RepositoryConfig.nameSnapShot } else { RepositoryConfig.nameRelease }
                repo.isAllowInsecureProtocol = true
                repo.url = URI(if (isSnapshot) { RepositoryConfig.urlSnapShot } else { RepositoryConfig.urlRelease })
                repo.credentials { cred ->
                    cred.username = "admin"
                    cred.password = "123456"
                }
            }
        }
    }

}
