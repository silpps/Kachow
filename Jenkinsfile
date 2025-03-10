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
