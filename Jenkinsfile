pipeline {
  agent {
    label 'Node 1'
  }
  tools {
    maven 'maven'
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
        sh 'mvn package'
        sh 'docker build -t sovanseyha/devops-spring-test:${BUILD_NUMBER} .'
      }
    }
    stage('Run docker image') {
      steps {
        sh 'docker run --name test -d -p 8080:8080 sovanseyha/devops-spring-test:${BUILD_NUMBER}'
      }
    }
    stage('Test') {
      steps {
        echo "Testing..."
        sh 'mvn test'
      }
    }
    stage('Stop docker container') {
      steps {
        sh 'docker rm test -f'
      }
    }
    stage('Push docker image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
          sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
          sh "docker push sovanseyha/devops-spring-test:${BUILD_NUMBER}"
        }
      }
    }
    stage('Trigger Manifest Update') {
      steps {
        build job: 'updatemanifest', parameters: [string(name: 'DOCKERTAG', value: env.BUILD_NUMBER)]
      }
    }
  }
}
