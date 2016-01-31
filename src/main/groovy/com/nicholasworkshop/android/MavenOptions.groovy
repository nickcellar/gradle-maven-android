package com.nicholasworkshop.android

import org.apache.maven.model.Developer
import org.apache.maven.model.License
import org.apache.maven.model.Scm
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Created by nickwph on 1/28/16.
 */

class MavenOptions {

    String name = "ActionBar-PullToRefresh Library"
    String packaging = 'aar'
    String description = "a modern implementation of the pull-to-refresh for android"
    String url = "https://github.com/chrisbanes/actionbar-pulltorefresh"
    Scm scm = new Scm()
    License license = new License()
    Developer developer = new Developer()
    NamedDomainObjectContainer<Target> targets;

    public MavenOptions(Project project) {
        targets = project.container(Target)
    }

    public void targets(Action<NamedDomainObjectContainer<Target>> action) {
        action.execute(targets)
    }
}

