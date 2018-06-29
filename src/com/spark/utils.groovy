#! /usr/bin/groovy
package com.spark

class utils {
    // checkout仓库的分支 如果这里不指定，默认用业务分支
    static String branch(project) {
        switch (project) {
            case "Backend-Go/camel":
                return 'master'
            case "Backend-Go/context":
                return 'master'
            case "Backend-Go/vicuna":
                return 'master'
            case "Backend-Go/xpay":
                return 'master'
            case "golang/xnsq":
                return 'master'
            default:
                return ""
        }
    }

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
