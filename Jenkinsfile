pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Kachow.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
