package com.spark

def checkout(CHECKOUT_LIST) {
    node {
        stage("Checkout") {
            echo "Checkout:"

            sh "$GO get -u -v gopkg.in/mgo.v2"

            script {

                for (i = 0; i  < CHECKOUT_LIST.size(); i++) {
                    def  project = CHECKOUT_LIST[i]
                    def PROJECT_DIR = utils.projectDir("${GOPATH}", project)

                    sh "mkdir -p ${PROJECT_DIR}"

                    def branch = utils.branch(project)
                    if (branch == "") {
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