package com.nicholasworkshop.android;

import org.gradle.api.Plugin;
import org.gradle.api.internal.project.ProjectInternal;

/**
 * Created by nickwph on 1/29/16.
 */
public class ArtifactIdPlugin implements Plugin<ProjectInternal> {

    private ProjectInternal project

    void apply(ProjectInternal project) {
        this.project = project
        project.ext {
            id = this.&id
            getId = this.&getId
            hasId = this.&hasId
        }
    }

    void id(param) {
        project.ext.artifact = param
    }

    String getId() {
        return project.hasProperty('artifact') ? project.ext.artifact : null
    }

    boolean hasId() {
        return project.hasProperty('artifact')
    }
}