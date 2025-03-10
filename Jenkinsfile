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
                sh 'mvn clean package'
            }
        }
        stage('Test & Coverage') {
            steps {
                sh 'mvn test'
                sh 'mvn jacoco:report'
            }
        }
        stage('Publish Coverage Report') {
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
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build successful! JaCoCo report generated.'
        }
        failure {
            echo 'Build failed. Check errors!'
        }
    }
}
