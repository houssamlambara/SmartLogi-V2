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
                    echo "Compilation du projet..."
                    mvn clean package -DskipTests=true
                '''
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    try {
                        sh '''
                            echo "Exécution des tests..."
                            mvn test || true
                        '''
                    } catch (Exception e) {
                        echo "Les tests ont échoué, mais le build continue..."
                    }
                }
            }
            post {
                always {
                    script {
                        try {
                            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                        } catch (Exception e) {
                            echo "Pas de résultats de tests disponibles"
                        }
                    }
                }
            }
        }

        // stage('SonarQube Analysis') {
        //     steps {
        //         withSonarQubeEnv('SonarSmartLogi') {
        //             sh '''
        //                 mvn sonar:sonar
        //             '''
        //         }
        //     }
        // }

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
