pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "smartlogi-api:latest"
        SONARQUBE_SERVER = "SonarSmartLogi"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'CI-CD',
                url: 'https://github.com/houssamlambara/SmartLogi-V2',
                credentialsId: 'github-token'
            }
        }

        stage('Verify Docker') {
            steps {
                sh '''
                    echo "Vérification de Docker..."
                    docker --version
                    docker ps
                '''
            }
        }

        stage('Build & Package') {
            steps {
                sh '''
                    docker run --rm \
                    -v "$PWD":/app \
                    -w /app \
                    maven:3.9-eclipse-temurin-17 \
                    mvn clean package -DskipTests=false
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                    docker run --rm \
                    -v "$PWD":/app \
                    -w /app \
                    maven:3.9-eclipse-temurin-17 \
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
                        docker run --rm \
                        -v "$PWD":/app \
                        -w /app \
                        maven:3.9-eclipse-temurin-17 \
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
