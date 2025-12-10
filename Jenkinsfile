pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        SONAR = credentials('sonar-token')
        KUBECONFIG_CREDENTIALS = credentials('kube-config')
        IMAGE = "shabaz/devops-app:${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps { git branch: 'main', url: 'https://github.com/your/repo.git' }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh '''
                    sonar-scanner                         -Dsonar.projectKey=advanced-devops                         -Dsonar.sources=.                         -Dsonar.login=$SONAR
                    '''
                }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Build Docker Image') {
            steps { sh 'docker build -t $IMAGE .' }
        }
        stage('Trivy Scan') {
            steps { sh 'trivy image --exit-code 1 --severity HIGH $IMAGE || true' }
        }
        stage('Push to Docker Hub') {
            steps {
                sh '''
                echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                docker push $IMAGE
                '''
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                mkdir -p ~/.kube
                echo "$KUBECONFIG_CREDENTIALS" > ~/.kube/config
                sed -i "s|shabaz/devops-app:latest|$IMAGE|g" k8s/deployment.yaml
                kubectl apply -f k8s/deployment.yaml
                kubectl apply -f k8s/service.yaml
                '''
            }
        }
    }
}
