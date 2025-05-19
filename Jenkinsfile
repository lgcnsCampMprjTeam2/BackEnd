pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "suhyunkim7288/CSrrr:latest"
        EC2_HOST = "ubuntu@13.125.34.183"
        SSH_CRED_ID = "ec2-key"
        OPENAI_API_KEY = credentials('OPENAI_API_KEY')
    }

    stages {
        stage('Git Checkout') {
            steps {
                git 'https://github.com/lgcnsCampMprjTeam2/BackEnd.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                sh '''
                docker build -t $DOCKER_IMAGE .
                echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                docker push $DOCKER_IMAGE
                '''
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent([SSH_CRED_ID]) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $EC2_HOST '
                      IDLE_PORT=$(curl -s http://localhost/profile | grep set1 >/dev/null && echo 8082 || echo 8081)

                      docker stop app-$IDLE_PORT || true
                      docker rm app-$IDLE_PORT || true
                      docker pull $DOCKER_IMAGE
                      docker run -d -p $IDLE_PORT:8080 --name app-$IDLE_PORT $DOCKER_IMAGE

                      for i in {1..10}; do
                        sleep 5
                        if curl -s http://localhost:$IDLE_PORT/profile | grep deploy; then
                          echo "Health check 성공"
                          break
                        fi
                      done

                      ~/switch_nginx.sh $IDLE_PORT
                    '
                    '''
                }
            }
        }
    }
}
