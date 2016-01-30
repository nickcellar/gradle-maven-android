package com.nicholasworkshop.android

import org.apache.maven.project.MavenProject
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenResolver
import org.gradle.api.plugins.MavenPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Upload
import org.gradle.plugins.signing.SigningPlugin
/**
 * Created by nickwph on 1/22/16.
 */

class RemoteMavenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        PluginContainer container = project.getPlugins()
        if (!container.hasPlugin(MavenPlugin)) {
            container.apply(MavenPlugin)
        }
        if (!container.hasPlugin(SigningPlugin)) {
            container.apply(SigningPlugin)
        }
        project.extensions.create('mavenOptions', MavenOptionsExtension, project)
        project.afterEvaluate(new AfterEvaluateAction())
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    class AfterEvaluateAction implements Action<Project> {
        @Override
        void execute(Project project) {
            MavenOptionsExtension options = project.mavenOptions
            options.targets.each { Repository target ->

                // create task
                String taskName = "publish${target.name.capitalize()}Release"
                Upload task = project.tasks.create(taskName, Upload.class)
                task.group = 'publish'

                MavenResolver resolver = task.repositories.mavenDeployer
                resolver.pom.groupId = project.group
                resolver.pom.artifactId = project.id
                resolver.pom.version = project.version

                MavenProject mavenProject = resolver.pom.project
                mavenProject.name = options.name
                mavenProject.packaging = options.packaging
                mavenProject.description = options.description
                mavenProject.url = options.url
                mavenProject.scm = options.scm
                mavenProject.licenses.add(options.license)
                mavenProject.developers.add(options.developer)
            }
        }
    }
}
