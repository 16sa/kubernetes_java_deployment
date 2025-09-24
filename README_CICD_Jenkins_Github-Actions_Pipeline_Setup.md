
======================Jenkins=======================

Step1: First we need to follow these steps from Step 1 to Step 13 https://github.com/16sa/Java_app/blob/main/README_CI_Pipeline_SETUP.md to setup a CI jenkins pipeline

Step 2: We need to install the plugin on our Jenkins server to make the withKubeConfig command available to your pipeline.
Navigate to Manage Jenkins on your Jenkins dashboard ==> Go to Manage Plugins ==> Click the Available plugins tab ==> In the search bar, 
type Kubernetes Credentials , kubernetes, kubernetes cli Plugin ==> Select the plugin and choose to install it ==> Restart your Jenkins 
instance for the changes to take effect.

Step 3: We need to Setup a kuberntes AWS EKS cluster using these steps https://github.com/16sa/kubernetes_java_deployment/blob/main/README.md. For now we can disregard the parts related to Helm or deployment of application.

Step 4: we need to create a Jenkins credential with the ID kubernetes-credentials that contains the configuration needed to connect to our Kubernetes EKS cluster.
Go to Manage Jenkins -> Manage Credentials ==>Select your credentials store (usually the (global) domain) ==> Click Add Credentials ==> 
From the dropdown, choose Secret file ==> In the "File" field, upload your kubeconfig file. 
This file contains the necessary cluster details, user information, and context to connect to our Kubernetes cluster.
Give it the ID kubernetes-credentials to match the name our  pipeline is looking for==> Click Create.
we can run this command to get the content of kubeconfig file: aws eks update-kubeconfig --region <your-region> --name <your-cluster-name>

Step 5 – Create a pipeline Job named demo for example and Add pipeline script as SCM

New Item → name, pipeline → ok
go to configure → select pipeline → select pipeline script with scm → SCM: git → paste your git repo → 
change branch name with main or any other branch → save

Option: we can configure a webhook in our GitHub repository's settings to send a real-time notification to a specific URL in our Jenkins server whenever a commit is pushed.

====================Github Actions=====================


Step 1: For Github Actions, Before creating the workflow, you need to set up a few things in your GitHub repository:

Repository Secrets: Go to your repository's Settings > Secrets and variables > Actions and add the following secrets. These are the equivalent of Jenkins credentials.

DOCKERHUB_USERNAME: Your Docker Hub username.
DOCKERHUB_TOKEN: Your Docker Hub access token. It's better to use a token than your password. generate one if you do not already have one, with read and write permission
SONAR_TOKEN: Your SonarQube authentication token.
SONAR_HOST_URL: The URL of your SonarQube instance
AWS_ACCESS_KEY_ID: Get from IAM user in AWS with EKS access
AWS_SECRET_ACCESS_KEY: Corresponding access key
AWS_REGION

Make sure the IAM user has permission to access EKS clusters and manage resources (e.g., AmazonEKSClusterPolicy and AmazonEKSWorkerNodePolicy).

Step 2: you need then to create the .github/workflows directory yourself. GitHub Actions are triggered by YAML files located in this specific directory at the root of your repository.

Go to your repository on GitHub.com.
Click on the Actions tab.
GitHub might suggest some templates. You can ignore them and click the "set up a workflow yourself" 
GitHub will automatically create the .github/workflows directory and a new file named main.yml for you in its web editor.
You can then delete the default content and paste your workflow code directly into the editor.
Click "Commit changes..." to save the file to your repository.

Step 3: Run the workflow 

Because we used on: workflow_dispatch, the workflow will only start when you tell it to.

Go to the "Actions" Tab: In your GitHub repository, click on the Actions tab.
Select Your Workflow: On the left sidebar, you'll see the name of your workflow ("CI/CD Pipeline for Java App"). Click on it.
Run the Workflow: You will see a banner with a "Run workflow" button. Click this button. A dropdown will appear with the inputs you defined (APP_NAME, action, ImageTag).
Fill in the Inputs: Enter the application name, choose either create or delete from the dropdown, and specify an image tag.
Start the Run: Click the green "Run workflow" button in the dropdown.

