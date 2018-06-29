#!/usr/bin/groovy
package com.spark

import com.spark.utils

def checkout(CHECKOUT_LIST) {
    node {
        stage("Checkout") {
            echo "Checkout:"

            sh "$GO get -u -v gopkg.in/mgo.v2"

            script {
                // Groovy map 迭代
                CHECKOUT_LIST.each { k, v ->
                    def PROJECT_DIR = utils.projectDir("${GOPATH}", k)
                    sh "mkdir -p ${PROJECT_DIR}"

                    def branch = v
                    if (v == "") {
                        branch = params.Branch
                    }

                    echo "project: ${project}, branch: ${branch}"

                    dir("${PROJECT_DIR}") {
                        git branch: branch,
                                credentialsId: 'phtanus',
                                url: utils.repo(project)
                    }
                }
            }
        }
    }
}