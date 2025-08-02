pipeline {
    agent any
    environment {
        DB_URL = credentials('ecommerce-db-url')
        DB_USERNAME = credentials('ecommerce-db-username')
        DB_PASSWORD = credentials('ecommerce-db-password')
    }
    stages {
        stage('Switch to Root and Prepare Workspace') {
            steps {
                sh '''
                    # Ensure sudo is available
                    if ! command -v sudo >/dev/null 2>&1; then
                        echo "sudo not found, please ensure it is installed"
                        exit 1
                    fi
                    # Switch to root for workspace preparation
                    sudo -n whoami | grep -q root || { echo "Failed to switch to root"; exit 1; }
                    # Clean workspace and fix permissions as root
                    sudo cleanWs()
                    sudo chown -R $(whoami):$(whoami) . || true
                    sudo chmod -R u+w .
                '''
            }
        }
        stage('Checkout SCM') {
            steps {
                git url: 'https://github.com/sushan2040/E-Commerce-Complete.git', branch: 'main'
            }
        }
        stage('Build Backend') {
            agent {
                docker {
                    image 'maven:3.8.6-eclipse-temurin-17'
                    reuseNode true
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""'
                }
            }
            steps {
                sh '''
                    cd E-Commerce
                    mvn clean install
                    apt-get update && apt-get install -y docker.io
                    docker build -t ecommerce-backend:latest .
                '''
            }
        }
        stage('Build Frontend') {
            agent {
                docker {
                    image 'node:20-alpine'
                    reuseNode true
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""'
                }
            }
            steps {
                sh '''
                    cd ecommerce
                    npm install --legacy-peer-deps
                    npm run build || true
                    apt-get update && apt-get install -y docker.io
                    if [ -d "./build" ]; then
                        echo "Build directory exists, building frontend image..."
                        docker build -t ecommerce-frontend:latest .
                    else
                        echo "Error: Build directory not found!"
                        exit 1
                    fi
                '''
            }
        }
        stage('Deploy') {
            agent {
                docker {
                    image 'node:20-alpine'
                    reuseNode true
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""'
                }
            }
            steps {
                sh '''
                    # Stop and remove existing containers if they exist
                    docker stop ecommerce-backend ecommerce-frontend || true
                    docker rm ecommerce-backend ecommerce-frontend || true

                    # Run backend container
                    docker run -d --name ecommerce-backend -p 8081:8081 \
                        -e DB_URL=$DB_URL \
                        -e DB_USERNAME=$DB_USERNAME \
                        -e DB_PASSWORD=$DB_PASSWORD \
                        ecommerce-backend:latest

                    # Run frontend container
                    docker run -d --name ecommerce-frontend -p 80:80 \
                        --link ecommerce-backend:backend \
                        ecommerce-frontend:latest

                    # Wait for services to start
                    sleep 10

                    # Verify deployments
                    if curl -s http://localhost:8081 > /dev/null; then
                        echo "Backend deployment successful!"
                    else
                        echo "Backend failed to start"
                        exit 1
                    fi
                    if curl -s http://localhost > /dev/null; then
                        echo "Frontend deployment successful!"
                    else
                        echo "Frontend not accessible"
                        exit 1
                    fi
                '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                // Add test commands if needed
            }
        }
    }
    post {
        always {
            // Delete folders if build fails or completes
            sh 'rm -rf E-Commerce ecommerce || true'
            echo "Cleaned up E-Commerce and ecommerce folders."
        }
        success {
            // Confirmation on success
            echo "Build and deployment successful. Folders already cleaned."
        }
        failure {
            // Log failure and rely on always block for cleanup
            echo "Build failed. Folders cleaned as part of always block."
            // Stop and remove containers on failure
            sh 'docker stop ecommerce-backend ecommerce-frontend || true'
            sh 'docker rm ecommerce-backend ecommerce-frontend || true'
        }
    }
}