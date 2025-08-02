pipeline {
    agent any
    triggers {
        pollSCM('* * * * * ') // Polls every second (requires Jenkins 2.263.1 or later)
    }
    stages {
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
                      args "-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=''"
                }
            }
            steps {
                sh '''
                    cd E-Commerce
                    mvn clean install
                    PID=$(sudo lsof -i :8081 -t)
                    if [ -n "$PID" ]; then
                        echo "Found E-Commerce running with PID: $PID"
                        echo "Attempting to terminate E-Commerce (PID: $PID) gracefully..."
                        sudo kill "$PID"
                        sleep 2
                        if ps -p "$PID" > /dev/null; then
                            echo "Process still running, forcing termination..."
                            sudo kill -9 "$PID"
                        fi
                        if ! ps -p "$PID" > /dev/null; then
                            echo "Previous E-Commerce instance terminated."
                        else
                            echo "Failed to terminate E-Commerce. Please check manually."
                            exit 1
                        fi
                    else
                        echo "No E-Commerce instance found running on port 8081."
                    fi
                    cd target/
                    nohup java -jar ecommerce-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
                    echo "Backend deployment successful!"
                '''
            }
        }
        stage('Build Frontend') {
            agent {
                docker {
                    image 'node:20-alpine'
                    reuseNode true
                      args "-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=''"
                }
            }
            steps {
                sh '''
                    cd ecommerce
                    npm install --legacy-peer-deps
                    npm run build
                    sudo cp -r ./build/* /var/www/html
                    sudo systemctl restart nginx
                    echo "Frontend deployment successful!"
                '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                // Add test commands, e.g., sh 'mvn test' for backend or sh 'npm run test' for frontend
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Deployment already handled in Build Backend/Frontend; add more steps if needed
            }
        }
    }
}