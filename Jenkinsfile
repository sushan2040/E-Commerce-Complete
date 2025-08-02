pipeline {
    agent any
    environment {
        DB_URL = credentials('ecommerce-db-url')
        DB_USERNAME = credentials('ecommerce-db-username')
        DB_PASSWORD = credentials('ecommerce-db-password')
        PASSWORD=credentials('sudo-password')
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
                        echo "$SUDO_PASSWORD" | sudo -S chown -R $(whoami):$(whoami) . || true
                        echo "$SUDO_PASSWORD" | sudo -S chmod -R u+w .
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
    }
}