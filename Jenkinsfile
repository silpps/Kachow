
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
        stage('JaCoCo Report') {
            steps {
                jacoco(
                    execPattern: '**/jacoco.exec',
                    classPattern: '**/classes',
                    sourcePattern: '**/src/main/java',
                    classDirectories: [[pattern: '**/classes']],
                    sourceDirectories: [[pattern: '**/src/main/java']]
                )
            }
        }
    }
    post {
        always {
            jacoco()
        }
    }
}
