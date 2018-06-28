package com.spark

def clean(params) {
    node {
        try {
            sh "docker images | grep none | awk \'{print \$3}\' | xargs docker rmi -f"
        } catch(e) {
            echo "Clean file error!"
        }
        if (params.Clean == "YES") {
            deleteDir()
        }
    }
}