Step1: First we need to follow these steps from Step 1 to Step 13 https://github.com/16sa/Java_app/blob/main/README.md to setup a CI jenkins pipeline

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