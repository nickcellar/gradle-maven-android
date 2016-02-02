package com.nicholasworkshop.android

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment
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
        project.extensions.create('mavenOptions', MavenOptions, project)
        project.afterEvaluate(new AfterEvaluateAction())
    }

    private class AfterEvaluateAction implements Action<Project> {
        @Override
        void execute(Project project) {
            MavenOptions options = validateOptions(project, (MavenOptions) project.mavenOptions)
            options.targets.each { Target target ->
                createMavenUploadTask(project, target, options)
            }
        }
    }

    private static MavenOptions validateOptions(Project project, MavenOptions options) {

        if (options.id == null) {
            if (project.plugins.hasPlugin(ArtifactIdPlugin) && project.hasId()) {
                options.id = project.getId()
            } else if (project.hasProperty('artifactId')) {
                options.id = project.ext.artifactId
            } else {
                throw new GradleException("Artifact id not set!")
            }
        }

        if (options.group == null) {
            if (project.group != null) {
                options.group = project.group
            } else {
                throw new GradleException("Group id not set!")
            }
        }

        if (options.version == null) {
            if (project.version != null) {
                options.version = project.version
            } else {
                throw new GradleException("Version not set!")
            }
        }

        if (options.projectName == null) {
            throw new GradleException("Project name not set!")
        }

        return options;
    }

    private Upload createMavenUploadTask(Project project, Target target, MavenOptions options) {
        String targetName = target.name.capitalize()
        String taskName = "publish${targetName}"
        Upload upload = project.tasks.create(taskName, Upload)
        upload.group = 'publish'
        upload.repositories {
            mavenDeployer {
                pom.groupId = options.group
                pom.artifactId = options.id
                pom.version = options.version
                pom.project {
                    name = options.projectName
                    packaging = options.packaging
                    description = options.description
                    url = options.url
                    scm {
                        url options.scm.url
                        connection options.scm.connection
                        developerConnection options.scm.developerConnection
                    }
                    licenses {
                        license {
                            name options.license.name
                            url options.license.url
                            distribution options.license.distribution
                        }
                    }
                    developers {
                        developer {
                            id options.developer.id
                            name options.developer.name
                        }
                    }
                }
                beforeDeployment {
                    MavenDeployment deployment -> signing.signPom(deployment)
                }
                repository(url: target.releaseUrl) {
                    authentication(userName: target.username, password: target.password)
                }
                snapshotRepository(url: target.snapshotUrl) {
                    authentication(userName: target.username, password: target.password)
                }
            }
        }
        project.signing {
            sign project.configurations.archives
            required {
                target.sign
            }
        }
        return upload
    }
}
