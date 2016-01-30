package com.nicholasworkshop.android

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.maven.Conf2ScopeMappingContainer
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectPublicationRegistry
import org.gradle.api.internal.artifacts.mvnsettings.LocalMavenRepositoryLocator
import org.gradle.api.internal.artifacts.mvnsettings.MavenSettingsProvider
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.MavenPluginConvention
import org.gradle.api.plugins.MavenRepositoryHandlerConvention
import org.gradle.api.publication.maven.internal.DefaultDeployerFactory
import org.gradle.api.publication.maven.internal.DefaultMavenRepositoryHandlerConvention
import org.gradle.api.publication.maven.internal.MavenFactory
import org.gradle.api.tasks.Upload
import org.gradle.configuration.project.ProjectConfigurationActionContainer
import org.gradle.internal.Factory
import org.gradle.logging.LoggingManagerInternal

import javax.inject.Inject

/**
 * Created by nickwph on 1/30/16.
 */
class LocalMavenPlugin implements Plugin<ProjectInternal> {

    private static final int COMPILE_PRIORITY = 300;
    private static final int RUNTIME_PRIORITY = 200;
    private static final int TEST_COMPILE_PRIORITY = 150;
    private static final int TEST_RUNTIME_PRIORITY = 100;
    private static final String INSTALL_TASK_NAME = "publishLocal";

    private final Factory<LoggingManagerInternal> loggingManagerFactory;
    private final FileResolver fileResolver;
    private final ProjectPublicationRegistry publicationRegistry;
    private final ProjectConfigurationActionContainer configurationActionContainer;
    private final MavenSettingsProvider mavenSettingsProvider;
    private final LocalMavenRepositoryLocator mavenRepositoryLocator;

    @Inject
    public LocalMavenPlugin(
            Factory<LoggingManagerInternal> loggingManagerFactory,
            FileResolver fileResolver,
            ProjectPublicationRegistry publicationRegistry,
            ProjectConfigurationActionContainer configurationActionContainer,
            MavenSettingsProvider mavenSettingsProvider,
            LocalMavenRepositoryLocator mavenRepositoryLocator) {
        this.loggingManagerFactory = loggingManagerFactory;
        this.fileResolver = fileResolver;
        this.publicationRegistry = publicationRegistry;
        this.configurationActionContainer = configurationActionContainer;
        this.mavenSettingsProvider = mavenSettingsProvider;
        this.mavenRepositoryLocator = mavenRepositoryLocator;
    }

    @Override
    void apply(ProjectInternal project) {
        project.getPluginManager().apply(BasePlugin.class);

        MavenFactory mavenFactory = project.getServices().get(MavenFactory.class);
        final MavenPluginConvention pluginConvention = addConventionObject(project, mavenFactory);
        final DefaultDeployerFactory deployerFactory = new DefaultDeployerFactory(
                mavenFactory,
                loggingManagerFactory,
                fileResolver,
                pluginConvention,
                project.getConfigurations(),
                pluginConvention.getConf2ScopeMappings(),
                mavenSettingsProvider,
                mavenRepositoryLocator);

        configureInstall(project.getConfigurations(), pluginConvention.getConf2ScopeMappings(), project, deployerFactory);
    }


    static MavenPluginConvention addConventionObject(ProjectInternal project, MavenFactory mavenFactory) {
        MavenPluginConvention mavenConvention = new MavenPluginConvention(project, mavenFactory);
        Convention convention = project.getConvention();
        convention.getPlugins().put("maven", mavenConvention);
        return mavenConvention;
    }


    static void configureInstall(ConfigurationContainer configurations, Conf2ScopeMappingContainer mavenScopeMappings, Project project2, DefaultDeployerFactory deployerFactory) {
        project2.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project project) {
                mavenScopeMappings.addMapping(
                        COMPILE_PRIORITY,
                        configurations.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME),
                        Conf2ScopeMappingContainer.COMPILE);
                mavenScopeMappings.addMapping(
                        RUNTIME_PRIORITY,
                        configurations.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME),
                        Conf2ScopeMappingContainer.RUNTIME);
                mavenScopeMappings.addMapping(
                        TEST_COMPILE_PRIORITY,
                        configurations.getByName(JavaPlugin.TEST_COMPILE_CONFIGURATION_NAME),
                        Conf2ScopeMappingContainer.TEST);
                mavenScopeMappings.addMapping(
                        TEST_RUNTIME_PRIORITY,
                        configurations.getByName(JavaPlugin.TEST_RUNTIME_CONFIGURATION_NAME),
                        Conf2ScopeMappingContainer.TEST);
                //
                Upload installUpload = project.getTasks().create(INSTALL_TASK_NAME, Upload.class);
                installUpload.group = 'publish'
                //
                RepositoryHandler repositories = installUpload.getRepositories();
                DefaultRepositoryHandler handler = (DefaultRepositoryHandler) repositories;
                DefaultMavenRepositoryHandlerConvention repositoryConvention = new DefaultMavenRepositoryHandlerConvention(handler, deployerFactory);
                new DslObject(repositories).getConvention().getPlugins().put("maven", repositoryConvention);
                //
                Configuration configuration = project.getConfigurations().getByName(Dependency.ARCHIVES_CONFIGURATION);
                installUpload.setConfiguration(configuration);
                MavenRepositoryHandlerConvention repositories2 = new DslObject(installUpload.getRepositories()).getConvention().getPlugin(MavenRepositoryHandlerConvention.class);
                repositories2.mavenInstaller();
                installUpload.setDescription("Installs the 'archives' artifacts into the local Maven repository.");
            }
        });
    }
}
