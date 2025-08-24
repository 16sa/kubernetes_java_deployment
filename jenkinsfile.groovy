@Library('jenkins_shared_library') _

pipeline {
    agent any

    parameters {
        string(name: 'APP_NAME', description: "Name of the application for the CI process (e.g., productcatalogue, shopfront, stockmanager)")
        choice(name: 'action', choices: 'create\ndelete', description: 'Choose create/Destroy')
        string(name: 'ImageTag', description: "tag of the docker build", defaultValue: 'v1')
        string(name: 'DockerHubUser', description: "name of the DockerHub user", defaultValue: 'N/A')
    }

    stages {
        stage('Checkout Monorepo') {
            when { expression { params.action == 'create' } }
            steps {
                git url: "https://github.com/16sa/kubernetes_java_deployment.git"
            }
        }
        
        stage('Unit Test maven') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        mvnTest()
                    }
                }
            }
        }

        stage('Integration Test maven') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        mvnIntegrationTest()
                    }
                }
            }
        }
        
        stage('Static code analysis: Sonarqube') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    def SonarQubecredentialsId = 'sonarqube-api'
                    dir("${params.APP_NAME}") {
                        statiCodeAnalysis(SonarQubecredentialsId)
                    }
                }
            }
        }
        
        stage('Quality Gate Status Check : Sonarqube') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    def SonarQubecredentialsId = 'sonarqube-api'
                    dir("${params.APP_NAME}") {
                        QualityGateStatus(SonarQubecredentialsId)
                    }
                }
            }
        }
        
        stage('Maven Build : maven') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        mvnBuild()
                    }
                }
            }
        }
        
        stage('Docker Image Build') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        dockerBuild("${params.APP_NAME}", "${params.ImageTag}", "${params.DockerHubUser}")
                    }
                }
            }
        }
        
        stage('Docker Image Scan: trivy') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        dockerImageScan("${params.APP_NAME}", "${params.ImageTag}", "${params.DockerHubUser}")
                    }
                }
            }
        }
        
        stage('Docker Image Push : DockerHub') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        dockerImagePush("${params.APP_NAME}", "${params.ImageTag}", "${params.DockerHubUser}")
                    }
                }
            }
        }
        
        stage('Docker Image Cleanup : DockerHub') {
            when { expression { params.action == 'create' } }
            steps {
                script {
                    dir("${params.APP_NAME}") {
                        dockerImageCleanup("${params.APP_NAME}", "${params.ImageTag}", "${params.DockerHubUser}")
                    }
                }
            }
        }
        
        stage('Deploy/Delete Microservice') {
            steps {
                script {
                    if (params.action == 'create') {
                        echo "Starting deployment of microservice: ${params.APP_NAME}"
                        
                        dir("kubernetes") {
                            // Update the image tag in the deployment file
                            sh "sed -i 's|image: .*|image: ${params.DockerHubUser}/${params.APP_NAME}:${params.ImageTag}|' ${params.APP_NAME}-deployment.yaml"
                            
                            withKubeConfig([credentialsId: 'kubernetes-credentials']) {
                                sh "kubectl apply -f ${params.APP_NAME}-service.yaml"
                            }
                        }
                    } else if (params.action == 'delete') {
                        echo "Starting deletion of microservice: ${params.APP_NAME}"
                        
                        dir("kubernetes") {
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