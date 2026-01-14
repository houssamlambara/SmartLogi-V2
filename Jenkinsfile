pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "smartlogi-api:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                sh '''
                    echo "=== Vérification de l'environnement ==="
                    echo "Workspace: $WORKSPACE"
                    docker --version
                    mvn --version
                    ls -la
                '''
            }
        }

        stage('Build & Package') {
            steps {
                sh '''
                    echo "=== Compilation du projet ==="
                    mvn clean package -DskipTests -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
                '''
            }
        }

        stage('Run Tests') {
            when {
                expression { return false }
            }
            steps {
                sh '''
                    echo "=== Tests désactivés ==="
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "=== Construction de l'image Docker ==="
                    docker build -t ${DOCKER_IMAGE} . --no-cache
                    echo "=== Vérification de l'image créée ==="
                    docker images | grep smartlogi-api
                '''
            }
        }

        stage('Verify JAR') {
            steps {
                sh '''
                    echo "=== Vérification du JAR généré ==="
                    ls -lh target/*.jar
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline CI/CD exécuté avec succès !'
            echo 'Image Docker créée : ${DOCKER_IMAGE}'
        }
        failure {
            echo 'Le pipeline a échoué. Consultez les logs ci-dessus.'
        }
        always {
            sh 'docker system prune -f || true'
        }
    }
}