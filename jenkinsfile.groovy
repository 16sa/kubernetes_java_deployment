@Library('jenkins_shared_library') _

pipeline{

    agent any

    parameters{
        string(name: 'APP_NAME', description: "Name of the application for the CI process (e.g., productcatalogue, shopfront, stockmanager)")
        choice(name: 'action', choices: 'create\ndelete', description: 'Choose create/Destroy')
        string(name: 'ImageTag', description: "tag of the docker build", defaultValue: 'v1')
        string(name: 'DockerHubUser', description: "name of the DockerHub user", defaultValue: 'safach')
    }

    stages{
        stage('Git Checkout'){
            when { expression { params.action == 'create' } }
            steps{
                gitCheckout(
                    branch: "main",
                    url: "https://github.com/16sa/kubernetes_java_deployment.git"
                )
            }
        }
        stage('Unit Test maven'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    mvnTest()
                }
            }
        }
        stage('Integration Test maven'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    mvnIntegrationTest()
                }
            }
        }
        stage('Static code analysis: Sonarqube'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    def SonarQubecredentialsId = 'sonarqube-api'
                    statiCodeAnalysis(SonarQubecredentialsId)
                }
            }
        }
        stage('Quality Gate Status Check : Sonarqube'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    def SonarQubecredentialsId = 'sonarqube-api'
                    QualityGateStatus(SonarQubecredentialsId)
                }
            }
        }
        stage('Maven Build : maven'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    dir("kubernetes-configmap-reload")
                        mvnBuild()
                }
            }
        }
        stage('Docker Image Build'){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    dockerBuild("${params.APP_NAME}","${params.ImageTag}","${params.DockerHubUser}")
                }
            }
        }
        stage('Docker Image Scan: trivy '){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    dockerImageScan("${params.APP_NAME}","${params.ImageTag}","${params.DockerHubUser}")
                }
            }
        }
        stage('Docker Image Push : DockerHub '){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    dockerImagePush("${params.APP_NAME}","${params.ImageTag}","${params.DockerHubUser}")
                }
            }
        }
        stage('Docker Image Cleanup : DockerHub '){
            when { expression { params.action == 'create' } }
            steps{
                script{
                    dockerImageCleanup("${params.APP_NAME}","${params.ImageTag}","${params.DockerHubUser}")
                }
            }
        }
        
        stage('Deploy/Delete Microservice') {
            steps {
                script {
                    def kubernetesManifestsRepo = 'https://github.com/16sa/kubernetes_java_deployment.git'
                    
                    // Checkout the Kubernetes manifests once
                    sh "git clone ${kubernetesManifestsRepo}"
                    
                    if (params.action == 'create') {
                        echo "Starting deployment of microservice: ${params.APP_NAME}"
                        
                        // Navigate to the correct directory and apply the manifests
                        dir("kubernetes_java_deployment/kubernetes") {
                            withKubeConfig([credentialsId: 'kubernetes-credentials']) {
                                sh "kubectl apply -f ${params.APP_NAME}-service.yaml"
                            }
                        }

                    } else if (params.action == 'delete') {
                        echo "Starting deletion of microservice: ${params.APP_NAME}"
                        
                        // Navigate to the correct directory and delete the manifests
                        dir("kubernetes_java_deployment/kubernetes") {
                            withKubeConfig([credentialsId: 'kubernetes-credentials']) {
                                sh "kubectl delete -f ${params.APP_NAME}-service.yaml"
                            }
                        }
                    }
                }
            }
        }
    }
}