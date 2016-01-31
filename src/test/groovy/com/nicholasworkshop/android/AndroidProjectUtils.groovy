package com.nicholasworkshop.android

import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder

/**
 * Created by nickwph on 1/23/16.
 */
class AndroidProjectUtils {

    static Project createAndroidApplicaitonProject() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        linkAndroidSdkDir(project)
        project.apply(plugin: 'com.android.application')
        project.android.compileSdkVersion 23
        project.android.buildToolsVersion "23.0.1"
        return project
    }

    static Project createAndroidLibraryProject() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        linkAndroidSdkDir(project)
        generateAndroidManifest(project)
        project.apply(plugin: 'com.android.library')
        project.android.compileSdkVersion 23
        project.android.buildToolsVersion "23.0.1"
        return project
    }

    private static void generateAndroidManifest(Project project) {
        File path = new File(project.projectDir.toString(), "src/main")
        File file = new File(path.toString(), "AndroidManifest.xml")
        path.mkdirs()
        file.createNewFile()
        file << "<manifest/>"
    }

    private static void linkAndroidSdkDir(DefaultProject project) {
        File file = new File(project.projectDir.toString(), "local.properties")
        File local = new File("local.properties")
        if (local.exists()) {
            file << local.text
        } else {
            FileOutputStream stream = new FileOutputStream(file)
            Properties properties = new Properties()
            properties.setProperty("sdk.dir", System.getenv("ANDROID_HOME"))
            properties.store(stream, null)
            stream.close()
        }
    }
}
