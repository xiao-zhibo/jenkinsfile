@Library("github.com/xiao-zhibo/jenkinsfile@master")

// 阿里云镜像的名字
@groovy.transform.Field String REGISTRY_PROJECT_NAME = "kangaroo"

// 可执行文件的名字
@groovy.transform.Field String APP_NAME = "app-kangaroo"

// 项目的地址
@groovy.transform.Field String PROJECT = "Backend-Go/kangaroo"

// 阿里云镜像的版本
@groovy.transform.Field String DOCKER_VERSION = "docker-dev"

// 配置文件的分支
@groovy.transform.Field String SPARK_BRANCH = "docker-dev"

// 配置文件的仓库
@groovy.transform.Field String SPARK_REPO = "git@gitlab.xinghuolive.com:Backend/spark.conf.d.git"

// main所在的路径
@groovy.transform.Field String BUILD_PATH = "Backend-Go/kangaroo"

// 需要checkout的仓库
@groovy.transform.Field List<String> CHECKOUT_LIST = [
        'Backend-Go/camel',
        'Backend-Go/kangaroo',
        'Backend-Go/vicuna',
        'Backend-Go/pigeon'
]

def pre = new com.spark.pre()
def checkout = new com.spark.checkout()
def build = new com.spark.build()
def publish = new com.spark.publish()
def clean = new com.spark.clean()

pipeline {
    agent any

    environment {
        BUILD = pwd()
        GOROOT = "/usr/local/go${params.GoVersion}/"
        GO = "${GOROOT}bin/go"
        GOPATH = "${BUILD}"
        SPARK_PATH = "${GOPATH}/spark.conf.d/"
    }

    parameters {
        choice(choices: '1.10.3\n1.9.7', description: 'Which Go Version?', name: 'GoVersion')
        choice(choices: 'v3.6.0\nmaster', description: 'Which branch u want to build?', name: 'Branch')
        choice(choices: 'NO\nYES', description: 'Whether to clean workspace after successful build or not', name: 'Clean')
    }

    options {
        disableConcurrentBuilds()
    }

    stages {
        stage("Start") {
            steps {
                script {
                    pre.pre(params, PROJECT, SPARK_BRANCH, SPARK_REPO)
                    checkout.checkout(CHECKOUT_LIST)
                    build.build(params, BUILD_PATH, PROJECT, APP_NAME)
                    publish.publish(BUILD_PATH, APP_NAME, REGISTRY_PROJECT_NAME, DOCKER_VERSION)
                }
            }
        }
    }
    post {
        success {
            script {
                clean.clean(params)
            }
        }
    }
}
