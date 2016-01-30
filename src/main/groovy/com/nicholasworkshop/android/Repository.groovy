package com.nicholasworkshop.android

/**
 * Created by nickwph on 1/30/16.
 */
class Repository {
    String name
    String releaseUrl = 'http://www.apache.org/licenses/license-2.0.txt'
    String snapshotUrl = 'http://www.apache.org/licenses/license-2.0.txt'
    String username = "scm:git@github.com:chrisbanes/actionbar-pulltorefresh.git"
    String password = "scm:git@github.com:chrisbanes/actionbar-pulltorefresh.git"

    void releaseUrl(String releaseUrl) {
        this.releaseUrl = releaseUrl
    }

    void snapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl
    }

    void username(String username) {
        this.username = username
    }

    void password(String password) {
        this.password = password
    }

    Repository(String name) {
        this.name = name
    }
}
