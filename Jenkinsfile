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
            agent {
                docker {
                    image 'mycompany/build-tools:latest' // Custom image with Maven, Node.js, Docker
                    reuseNode true
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=""'
                }
            }
            steps {
                sh '''
                    # Build Backend
                    cd E-Commerce
                    mvn clean install
                    docker build -t ecommerce-backend:latest .

                    # Build Frontend
                    cd ../ecommerce
                    npm install --legacy-peer-deps
                    npm run build
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
            steps {
                sh '''
                    docker network create ecommerce-network || true
                    docker stop ecommerce-backend ecommerce-frontend || true
                    docker rm ecommerce-backend ecommerce-frontend || true
                    docker run -d --name ecommerce-backend --network ecommerce-network -p 8081:8081 \
                        -e DB_URL=$DB_URL \
                        -e DB_USERNAME=$DB_USERNAME \
                        -e DB_PASSWORD=$DB_PASSWORD \
                        ecommerce-backend:latest
                    docker run -d --name ecommerce-frontend --network ecommerce-network -p 80:80 \
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
            agent {
                docker {
                    image 'mycompany/build-tools:latest'
                    reuseNode true
                    args '-u root'
                }
            }
            steps {
                parallel {
                    stage('Backend Tests') {
                        steps {
                            sh '''
                                cd E-Commerce
                                mvn test
                            '''
                        }
                    }
                    stage('Frontend Tests') {
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