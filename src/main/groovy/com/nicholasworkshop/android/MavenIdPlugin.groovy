package com.nicholasworkshop.android;

import org.gradle.api.Plugin;
import org.gradle.api.internal.project.ProjectInternal;

/**
 * Created by nickwph on 1/29/16.
 */
public class MavenIdPlugin implements Plugin<ProjectInternal> {

    private ProjectInternal project

    void apply(ProjectInternal project) {
        this.project = project
        project.ext {
            id = this.&id
            getId = this.&getId
        }
    }

    void id(param) {
        project.ext.artifact = param
    }

    String getId() {
        return project.ext.artifact
    }
}