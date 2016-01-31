package com.nicholasworkshop.android

import org.gradle.api.Project
import org.gradle.api.tasks.Upload
import org.junit.Assert
import org.junit.Test

/**
 * Created by nickwph on 1/23/16.
 */
class MavenPluginTest_Remote {

    @Test
    void testApply() throws Exception {
        Project project = AndroidProjectUtils.createAndroidApplicaitonProject()
        project.apply(plugin: 'com.nicholasworkshop.android.maven')
        project.evaluate()
    }

    @Test
    void testApply_withOptions() throws Exception {
        Project project = AndroidProjectUtils.createAndroidApplicaitonProject()
        project.apply(plugin: 'com.nicholasworkshop.android.maven')
        project.id 'id'
        project.mavenOptions {
            targets {
                sonatype {
                    releaseUrl 'releaseUrl'
                    snapshotUrl 'snapshotUrl'
                    username 'username'
                    password 'password'
                }
            }
        }
        project.evaluate()

        // verify
        Assert.assertTrue(project.tasks.getNames().contains("publishSonatype"))
        Upload upload = project.tasks.getByName("publishSonatype")
        Assert.assertNotNull(upload)
//        Assert.equals('releaseUrl', upload.uploadDescriptor.)
    }


}
