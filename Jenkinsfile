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
        sh 'docker build -t sovanseyha/springjenkins .'
      }
    }
    stage('Test') {
      steps {
        echo "Testing..."
        sh 'mvn test'
      }
    }
    stage('Deploy') {
      steps {
        sh 'docker run -d -p 8085:8080 sovanseyha/springjenkins'
      }
    }
  }
}
