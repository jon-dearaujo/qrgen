pipeline {
    agent { label 'docker-agent-alpine' }

    stages {
        stage('Build') {
            steps {
                sh '''./gradlew build clean'''
            }
        }
        stage('Test') {
            steps {
                sh '''./gradlew test'''
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
