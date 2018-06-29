#! /usr/bin/groovy
package com.spark

class utils {

    static String importPath(project) {
        return "gitlab.xinghuolive.com/${project}"
    }

    static String repo(project) {
        return "git@gitlab.xinghuolive.com:${project}.git"
    }

    static String projectDir(gopath, project) {
        return "${gopath}/src/gitlab.xinghuolive.com/${project}/"
    }
}
