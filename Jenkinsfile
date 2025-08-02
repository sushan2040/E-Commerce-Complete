pipeline {
    agent any
    environment {
        DB_URL = credentials('ecommerce-db-url')
        DB_USERNAME = credentials('ecommerce-db-username')
        DB_PASSWORD = credentials('ecommerce-db-password')
        REACT_APP_API_URL=credentials('ecommerce-api-url')
    }
    stages {
        stage('Switch to Root and Prepare Workspace') {
            steps {
                sh '''
                    if ! command -v sudo >/dev/null 2>&1; then
                        echo "sudo not found, please ensure it is installed"
                        exit 1
                    fi
                    sudo -n whoami | grep -q root || { echo "Failed to switch to root; ensure passwordless sudo is configured for Jenkins user"; exit 1; }
                    sudo -n chown -R $(whoami):$(whoami) . || true
                    sudo -n chmod -R u+w .
                '''
            }
        }
        stage('Checkout SCM') {
            steps {
                git url: 'https://github.com/sushan2040/E-Commerce-Complete.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                script {
                    // Backend build in maven image
                    docker.image('maven:3.8.6-eclipse-temurin-17').inside('-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""') {
                        sh '''
                            cd E-Commerce
                            mvn clean install
                        '''
                    }
                    // Frontend build in node image
                    docker.image('node:20-alpine').inside('-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""') {
                        sh '''
                            cd ecommerce
                            npm install --legacy-peer-deps
                            npm run build || true
                            if [ -d "./build" ]; then
                                echo "Build directory exists, building frontend image..."
                            else
                                echo "Error: Build directory not found!"
                                exit 1
                            fi
                        '''
                    }
                }
            }
        }
        stage('Build Docker Images') {
            agent {
                docker {
                    image 'docker:27.1.1'
                    reuseNode true
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""'
                }
            }
            steps {
                sh '''
                docker build -t ecommerce-backend:latest ./E-Commerce
                    docker build -t ecommerce-frontend:latest ./ecommerce
                    docker network create ecommerce-network || true
                    docker stop ecommerce-backend ecommerce-frontend || true
                    docker rm ecommerce-backend ecommerce-frontend || true
                    docker run -d --name ecommerce-backend --network ecommerce-network -p 8081:8081 \
                        -e DB_URL="$DB_URL" \
                        -e DB_USERNAME="$DB_USERNAME" \
                        -e DB_PASSWORD="$DB_PASSWORD" \
                        ecommerce-backend:latest
                    docker run -d --name ecommerce-frontend --network ecommerce-network -p 80:80 \
                        ecommerce-frontend:latest
                    sleep 10
                     # Check backend deployment
                    if [ "$(docker inspect --format '{{.State.Running}}' ecommerce-backend)" = "true" ] && \
                       docker inspect --format '{{.NetworkSettings.Ports}}' ecommerce-backend | grep -q "8081"; then
                        echo "Backend deployment successful!"
                    else
                        echo "Backend failed to start or port 8081 not mapped"
                        exit 1
                    fi
                    # Check frontend deployment
                    if [ "$(docker inspect --format '{{.State.Running}}' ecommerce-frontend)" = "true" ] && \
                       docker inspect --format '{{.NetworkSettings.Ports}}' ecommerce-frontend | grep -q "80"; then
                        echo "Frontend deployment successful!"
                    else
                        echo "Frontend failed to start or port 80 not mapped"
                        exit 1
                    fi
                '''
            }
        }
    }
    post {
        always {
            sh '''
                sudo -n rm -rf E-Commerce ecommerce || true
                echo "Cleaned up E-Commerce and ecommerce folders."
            '''
        }
        success {
            archiveArtifacts artifacts: 'E-Commerce/target/*.jar', allowEmptyArchive: true
            echo "Build and deployment successful. Folders already cleaned."
        }
        failure {
            sh '''
                echo "Build failed. Folders cleaned as part of always block."
                docker stop ecommerce-backend ecommerce-frontend || true
                docker rm ecommerce-backend ecommerce-frontend || true
            '''
        }
    }
}