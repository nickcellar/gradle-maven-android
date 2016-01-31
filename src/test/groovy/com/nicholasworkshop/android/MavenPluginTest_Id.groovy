package com.nicholasworkshop.android

import org.gradle.api.Project
import org.junit.Assert
import org.junit.Test

/**
 * Created by nickwph on 1/23/16.
 */
class MavenPluginTest_Id {
    @Test
    void testApply() throws Exception {
        Project project = AndroidProjectUtils.createAndroidApplicaitonProject()
        project.apply(plugin: 'com.nicholasworkshop.android.maven')
        project.id 'abcde'
        project.evaluate()
        Assert.assertEquals('abcde', project.getId())
    }

    @Test
    void testApply_onLibraryProject() throws Exception {
        Project project = AndroidProjectUtils.createAndroidLibraryProject()
        project.apply(plugin: 'com.nicholasworkshop.android.maven')
        project.id 'abcde'
        project.evaluate()
        Assert.assertEquals('abcde', project.getId())
    }
}
