pipeline {
    agent any

    environment {
        MVN_HOME = '/usr/share/maven'
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

        stage('Build Maven') {
            steps {
                sh 'mvn clean install -DskipTests=false'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
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
                    sh 'mvn sonar:sonar'
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
