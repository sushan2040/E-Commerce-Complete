pipeline {
    agent any
    environment {
        DB_URL = credentials('ecommerce-db-url')
        DB_USERNAME = credentials('ecommerce-db-username')
        DB_PASSWORD = credentials('ecommerce-db-password')
    }
    stages {
        stage('Prepare Workspace') {
            steps {
                  cleanWs()// Clean the workspace before any operations
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
                    # Check if port 8081 is in use (alternative without lsof)
                    PORT_IN_USE=$(netstat -tulpn 2>/dev/null | grep :8081 || true)
                    if [ -n "$PORT_IN_USE" ]; then
                        echo "Port 8081 is in use. Attempting to terminate the process..."
                        PID=$(netstat -tulpn 2>/dev/null | grep :8081 | awk '{print $7}' | cut -d'/' -f1)
                        if [ -n "$PID" ]; then
                            kill "$PID" 2>/dev/null || true
                            sleep 2
                            if kill -0 "$PID" 2>/dev/null; then
                                kill -9 "$PID" 2>/dev/null || true
                            fi
                            if ! kill -0 "$PID" 2>/dev/null; then
                                echo "Previous E-Commerce instance terminated."
                            else
                                echo "Failed to terminate E-Commerce on port 8081."
                                exit 1
                            fi
                        fi
                    else
                        echo "No E-Commerce instance found running on port 8081."
                    fi
                    cd target/
                    nohup java -jar ecommerce-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
                    sleep 10
                    if curl -s http://localhost:8081 > /dev/null; then
                        echo "Backend deployment successful!"
                    else
                        echo "Backend failed to start on port 8081"
                        cat app.log
                        exit 1
                    fi
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
                    if [ -d "./build" ]; then
                        echo "Build directory exists, copying files..."
                        mkdir -p /var/www/html
                        cp -r ./build/* /var/www/html/
                        echo "Files copied successfully."
                    else
                        echo "Error: Build directory not found!"
                        exit 1
                    fi
                    systemctl restart nginx || echo "Warning: systemctl not available in container; restart nginx manually on host"
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
                // Add test commands, e.g., sh 'cd E-Commerce && mvn test' or sh 'cd ecommerce && npm run test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Deployment handled in Build stages; add more steps if needed
            }
        }
    }
    post {
            always {
                // Delete folders if build fails
                sh 'rm -rf E-Commerce ecommerce || true'
                echo "Cleaned up E-Commerce and ecommerce folders."
            }
            success {
                // Additional cleanup or confirmation on success
                 sh 'rm -rf E-Commerce ecommerce || true'
                echo "Build and deployment successful. Folders already cleaned."
            }
            failure {
                // Ensure cleanup on failure
                 sh 'rm -rf E-Commerce ecommerce || true'
                echo "Build failed. Folders cleaned as part of always block."
            }
        }
}