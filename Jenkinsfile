pipeline {
    agent { label 'docker-agent-alpine' }

    stages {
        stage('Build') {
            steps {
                '''./gradlew build clean'''
            }
        }
        stage('Test') {
            steps {
                '''./gradlew test'''
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
