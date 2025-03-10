
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/silpps/Kachow.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Test & Coverage') {
            steps {
                bat 'mvn test'
                bat 'mvn jacoco:report'
            }
        }
        stage('Jacoco Coverage Report') {
            steps {
                jacoco execPattern: '**/target/jacoco.exec',
                       classPattern: '**/target/classes',
                       sourcePattern: '**/src/main/java',
                       changeBuildStatus: true
            }
        }
    }
    post {
        always {
            jacoco()
        }
    }
}
