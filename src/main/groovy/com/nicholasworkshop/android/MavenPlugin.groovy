package com.nicholasworkshop.android

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal

/**
 * Created by nickwph on 1/30/16.
 */
class MavenPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(ProjectInternal project) {
        project.pluginManager.apply(MavenIdPlugin);
        project.pluginManager.apply(LocalMavenPlugin);
        project.pluginManager.apply(RemoteMavenPlugin);
    }
}
