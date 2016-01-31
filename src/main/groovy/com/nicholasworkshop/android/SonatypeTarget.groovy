package com.nicholasworkshop.android;

/**
 * Created by nickwph on 1/26/16.
 */
public class SonatypeTarget implements Repo {

    @Override
    String getReleaseUrl() {
        return hasProperty('SONATYPE_RELEASE_REPOSITORY_URL') ? SONATYPE_RELEASE_REPOSITORY_URL
                : "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    }

    @Override
    String getSnapshotUrl() {
        return hasProperty('SONATYPE_SNAPSHOT_REPOSITORY_URL') ? SONATYPE_SNAPSHOT_REPOSITORY_URL
                : "https://oss.sonatype.org/content/repositories/snapshots/"
    }

    @Override
    String getUsername() {
        return hasProperty('SONATYPE_USERNAME') ? SONATYPE_USERNAME : "nickwph"
    }

    @Override
    String getPassword() {
        return hasProperty('SONATYPE_PASSWORD') ? SONATYPE_PASSWORD : "iamhei"
    }
}
