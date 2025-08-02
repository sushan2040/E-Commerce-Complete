pipeline {
    agent any
    stages {
        stage('Build Backend') {
            steps {
               agent {
                docker {
                    image 'maven:3.8.6-eclipse-temurin-17'
                    reuseNode true
                }
            }
            steps {
                sh '''
                    git pull
                    cd E-Commerce
                    mvn clean install
                    PID=$(sudo lsof -i :8081 -t)

                    if [ -n "$PID" ]; then
                    echo "Found E-Commerce running with PID: $PID"
  
                    # Try graceful termination
                    echo "Attempting to terminate E-Commerce (PID: $PID) gracefully..."
                    sudo kill "$PID"
  
                    # Wait for a few seconds to allow graceful shutdown
                    sleep 2
  
                    # Check if process is still running
                    if ps -p "$PID" > /dev/null; then
                    echo "Process still running, forcing termination..."
                    sudo kill -9 "$PID"
                    fi
  
                    # Verify termination
                    if ! ps -p "$PID" > /dev/null; then
                    echo "Previous E-Commerce instance terminated."
                    else
                    echo "Failed to terminate E-Commerce. Please check manually."
                exit 1
                fi
                else
                echo "No E-Commerce instance found running on port 8080."
                fi
                cd target/
                nohup java -jar ecommerce-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
                echo "Backend deployment successfull!"
                cd ..
                cd ..
                '''
            }
            }
        }
             stage('Build frontend') {
            steps {
               agent {
                docker {
                    image 'node:20-alpine'
                    reuseNode true
                }
            }
            steps {
                sh '''
                cd ecommerce/
                npm install --legacy-peer-deps
                npm run build
                sudo cp -r ./build/* /var/www/html
                systemctl restart nginx
                echo "Frontend deployment successfull!"
                    '''
            }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}