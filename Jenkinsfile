pipeline {
    agent any
    tools {
        maven 'maven'
    }
    environment {
        MY_IMAGE = 'seiha-spring-img'
        DOCKER_REGISTRY = 'sovanseyha'
        CONTAINER_NAME = 'jenkins-container'
        TELEGRAM_BOT_TOKEN = credentials('telegramToken')
        TELEGRAM_CHAT_ID = credentials('telegramChatid')
        PROJECT_NAME = 'Spring Jenkins'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    try {
                        echo "Starting Build stage"
                        sh 'mvn clean install'
                        sh 'mvn package'
                        sh "docker build -t ${DOCKER_REGISTRY}/${MY_IMAGE}:${BUILD_NUMBER} ."
                        echo "Build completed successfully"
                        currentBuild.result = 'SUCCESS'
                        sendToTelegram("✅ Build Succeeded for Build #${BUILD_NUMBER}")
                    } catch (Exception e) {
                        echo "Build failed with error: ${e}"
                        currentBuild.result = 'FAILURE'
                        currentBuild.description = e.toString()
                        sendToTelegram("❌ Build Failed for Build #${BUILD_NUMBER}\nError Message:\n${e.message}")
                        error("Build stage failed")
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    try {
                        echo "Starting Test stage"
                        def status = currentBuild.resultIsBetterOrEqualTo('SUCCESS') ? 'Succeed' : 'Failed'
                        sendToTelegram("🧪 Testing Status: ${status} for Build #${BUILD_NUMBER}")
                        echo "Test completed successfully"
                    } catch (Exception e) {
                        echo "Test failed with error: ${e}"
                        currentBuild.result = 'FAILURE'
                        currentBuild.description = e.toString()
                        sendToTelegram("❌ Testing Failed for Build #${BUILD_NUMBER}\nError Message:\n${e.message}")
                        error("Test stage failed")
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    try {
                        echo "Starting Deploy stage"
                        withCredentials([usernamePassword(credentialsId: 'dockerhub_id', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            // Log in to the Docker registry
                            sh "docker login -u \$DOCKER_USERNAME -p \$DOCKER_PASSWORD"

                            def existImageID = sh(script: "docker ps -aq -f name='${MY_IMAGE}'", returnStdout: true)
                            echo "ExistImageID:${existImageID}"
                            if (existImageID) {
                                echo "${existImageID} is removing ..."
                                sh "docker rm -f ${MY_IMAGE}"
                            } else {
                                echo 'No existing container'
                            }

                            // Use Docker Compose to deploy the application
                            sh 'docker compose build'
                            sh 'docker compose up -d'
                        }
                        def status = currentBuild.resultIsBetterOrEqualTo('SUCCESS') ? 'Succeed' : 'Failed'
                        sendToTelegram("🚀 Deployment Status: ${status} for Build #${BUILD_NUMBER}")
                        echo "Deploy completed successfully"
                    } catch (Exception e) {
                        echo "Deploy failed with error: ${e}"
                        currentBuild.result = 'FAILURE'
                        currentBuild.description = e.toString()
                        sendToTelegram("❌ Deployment Failed for Build #${BUILD_NUMBER}\nError Message:\n${e.message}")
                        error("Deploy stage failed")
                    }
                }
            }
        }
    }
    post {
        always {
            emailext body: 'Check console output at $BUILD_URL to view the results.', subject: '${PROJECT_NAME} - Build #${BUILD_NUMBER} - $BUILD_STATUS', to: 'yan.sovanseyha@gmail.com'
        }
    }
}

def sendToTelegram(message) {
    script {
        sh """
            curl -s -X POST https://api.telegram.org/bot\${TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=\${TELEGRAM_CHAT_ID} -d parse_mode="HTML" -d text="${message}"
        """
    }
}
