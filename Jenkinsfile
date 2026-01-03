pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "smartlogi-api:latest"
        SONARQUBE_SERVER = "SonarSmartLogi"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Docker') {
            steps {
                sh '''
                    echo "Vérification de Docker..."
                    docker --version
                    docker ps
                    echo "Workspace: $WORKSPACE"
                    ls -la
                '''
            }
        }

        stage('Build & Package') {
            steps {
                sh '''
                    mvn clean package -DskipTests=false
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                    mvn test
                '''
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco execPattern: '**/target/jacoco.exec'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarSmartLogi') {
                    sh '''
                        mvn sonar:sonar
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t smartlogi-api:latest .'
            }
        }
    }

    post {
        success {
            echo 'Build CI/CD réussi !'
        }
        failure {
            echo 'Le build a échoué. Vérifie les logs !'
        }
    }
}
