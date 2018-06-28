package com.spark

import com.spark.utils

def publish(BUILD_PATH, APP_NAME, REGISTRY_PROJECT_NAME, DOCKER_VERSION) {
    node {
        stage("Publish") {
            echo "Publish:"

            script {
                def workPath = "."
                def appFile = utils.projectDir("${GOPATH}", "${BUILD_PATH}") + APP_NAME
                def goLibPath = GOROOT + "lib/"

                sh "mkdir -p ${workPath}${GOROOT} && cp -rf ${goLibPath} ${workPath}${GOROOT}"
                sh "cp -rf ${appFile} ${workPath}"
                sh "mkdir -p ${workPath}/spark.conf.d/ && cp -rf ${SPARK_PATH} ${workPath}/spark.conf.d/"
                try {
                    sh "echo \"FROM centurylink/ca-certs\n\nMAINTAINER phtanus <sysu511@gmail.com>\n\nENV spark_path=/spark.conf.d\n\nCOPY ${APP_NAME} /\nADD spark.conf.d/ /spark.conf.d/\nADD ${goLibPath} ${goLibPath}\n\" > Dockerfile"
                } catch(e) {
                    echo "Generate Dockerfile error!"
                }

                def imageName = "registry.cn-shenzhen.aliyuncs.com/xiaozhibo/${REGISTRY_PROJECT_NAME}:${DOCKER_VERSION}"
                def dockerBuild = docker.build(imageName, "${workPath}")
                docker.withRegistry("https://registry.cn-shenzhen.aliyuncs.com", "48587c29-a56a-4c7d-af6c-1c9b6754b371") {
                    dockerBuild.push()
                }
            }
        }
    }
}