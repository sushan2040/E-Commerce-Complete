pipeline {
    agent any
    environment {
        DB_URL = credentials('ecommerce-db-url')
        DB_USERNAME = credentials('ecommerce-db-username')
        DB_PASSWORD = credentials('ecommerce-db-password')
        SUDO_PASSWORD = credentials('sudo-password')
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
                        # Verify root access
                        echo "$SUDO_PASSWORD" | sudo -S whoami | grep -q root || { echo "Failed to switch to root"; exit 1; }
                        # Clean workspace and fix permissions as root
                        echo "$SUDO_PASSWORD" | sudo -S cleanWs
                        echo "$SUDO_PASSWORD" | sudo -S chown -R $(whoami):$(whoami) . || true
                        echo "$SUDO_PASSWORD" | sudo -S chmod -R u+w .
                    '''
            }
        }
        stage('Checkout SCM') {
            steps {
                    sh '''
                        # Run git commands as root
                        echo "$SUDO_PASSWORD" | sudo -S rm -rf E-Commerce-Complete
                        echo "$SUDO_PASSWORD" | sudo -S git clone --branch main https://github.com/sushan2040/E-Commerce-Complete.git
                    '''
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
                '''
            }
        }
        stage('Build Backend Image') {
            steps {
                    sh '''
                        echo "$SUDO_PASSWORD" | sudo -S docker build -t ecommerce-backend:latest ./E-Commerce
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
                    npm run build
                '''
            }
        }
        stage('Build Frontend Image') {
            steps {
                    sh '''
                        echo "$SUDO_PASSWORD" | sudo -S docker build -t ecommerce-frontend:latest ./ecommerce
                    '''
                }
        }
        stage('Deploy') {
            steps {
                    sh '''
                        echo "$SUDO_PASSWORD" | sudo -S docker network create ecommerce-network || true
                        echo "$SUDO_PASSWORD" | sudo -S docker stop ecommerce-backend ecommerce-frontend || true
                        echo "$SUDO_PASSWORD" | sudo -S docker rm ecommerce-backend ecommerce-frontend || true
                        echo "$SUDO_PASSWORD" | sudo -S docker run -d --name ecommerce-backend --network ecommerce-network -p 8081:8081 \
                            -e DB_URL=$DB_URL \
                            -e DB_USERNAME=$DB_USERNAME \
                            -e DB_PASSWORD=$DB_PASSWORD \
                            ecommerce-backend:latest
                        echo "$SUDO_PASSWORD" | sudo -S docker run -d --name ecommerce-frontend --network ecommerce-network -p 80:80 \
                            ecommerce-frontend:latest
                        sleep 10
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
            parallel {
                stage('Backend Tests') {
                    agent {
                        docker {
                            image 'maven:3.8.6-eclipse-temurin-17'
                            reuseNode true
                            args '-u root'
                        }
                    }
                    steps {
                        sh '''
                            cd E-Commerce
                            mvn test
                        '''
                    }
                }
                stage('Frontend Tests') {
                    agent {
                        docker {
                            image 'node:20-alpine'
                            reuseNode true
                            args '-u root'
                        }
                    }
                    steps {
                        sh '''
                            cd ecommerce
                            npm test
                        '''
                    }
                }
            }
        }
    }
    post {
        always {
                sh '''
                    echo "$SUDO_PASSWORD" | sudo -S rm -rf E-Commerce ecommerce || true
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
                    echo "$SUDO_PASSWORD" | sudo -S docker stop ecommerce-backend ecommerce-frontend || true
                    echo "$SUDO_PASSWORD" | sudo -S docker rm ecommerce-backend ecommerce-frontend || true
                '''
        }
    }
}