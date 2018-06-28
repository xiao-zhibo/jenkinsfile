package com.spark

def build(params, BUILD_PATH, PROJECT, APP_NAME) {
    node {
        stage("Build") {
            echo "Build:"

            script {

                dir(utils.projectDir("${GOPATH}", BUILD_PATH)) {

                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    echo "git commit: ${gitCommit}"

                    def now = new Date().format("yyyy-MM-dd HH:mm:ss")
                    echo "now: ${now}"

                    def comment = sh(returnStdout: true, script: 'git log --oneline --format=%B -n 1 HEAD')
                    echo "comment: ${comment}"

                    def target = utils.importPath(PROJECT) + "/project"

                    List<String> ldflags = [
                            "-s",
                            "-w",
                            "-X",
                            "${target}.CODE_VERSION=${params.Branch}",
                            "-X",
                            "${target}.GO_VERSION=${params.GoVersion}",
                            "-X",
                            "\"${target}.BUILD_DATE_TIME=${now}\"",
                            "-X",
                            "${target}.GIT_COMMIT_HASH=${gitCommit}",
                            "-X",
                            "\"${target}.LAST_COMMIT_MESSAGE=${comment}\"",
                    ]

                    ldflagsStr = ldflags.join(' ')

                    def binPath = utils.importPath(BUILD_PATH)

                    sh "CGO_ENABLED=0 GOOS=linux GOARCH=amd64 ${GO} build -ldflags \'${ldflagsStr}\' -v -o ${APP_NAME} ${binPath}"
                }
            }
        }
    }
}