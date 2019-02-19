pipeline {
  agent {
    label 'jenkins-jenkins-slave'
  }
  
  environment {
    AWS_DEFAULT_REGION = 'us-east-1'
    AWS_PCI_ID = '691190359445'
    AWS_PROD_ID = '134937327130'
    AWS_SAND_ID = '135302193720'
    AWS_HMLG_ID = '290543716360'

  }
  parameters {
    string(defaultValue: 'mkid-app', description: 'App', name: 'App')
    string(defaultValue: 'prod', description: 'Insert the Environment', name: 'EnvironmentAws')
    string(defaultValue: '8080', description: 'Insert PortFront', name: 'PortFront')
    string(defaultValue: '80', description: 'Insert PortApp', name: 'PortApp')
    string(defaultValue: '1', description: 'Insert MinSize', name: 'MinSize')
    string(defaultValue: '1', description: 'Insert DesiredCapacity', name: 'DesiredCapacity')
    string(defaultValue: '1', description: 'Insert MaxSize', name: 'MaxSize')
  }

  stages {
    stage('Download') {
      steps {
        withCredentials(bindings: [string(credentialsId: 'GIT_TOKEN', variable: 'GitToken')]) {
          sh '''
              ls -l
              curl -H "Authorization: token ${GitToken}" -H \'Accept: application/vnd.github.v4.raw\' -O -L https://api.github.com/repos/moip/sre-shs/contents/scripts/teste-bag.py
              curl -H "Authorization: token ${GitToken}" -H \'Accept: application/vnd.github.v4.raw\' -O -L https://api.github.com/repos/moip/sre-shs/contents/scripts/deploy-bag.py
              curl -H "Authorization: token ${GitToken}" -H \'Accept: application/vnd.github.v4.raw\' -O -L https://api.github.com/repos/moip/sre-shs/contents/scripts/remove-teste-bag.py
          '''
        }
      }
    }
    stage('Build APP') {
      steps {
        sh '''
        cd /tmp/workspace/mockkid_ci-cd
        mvn clean install
        '''
      }
    }
    stage('Build Images') {
      steps {
          script {
            if ( params.EnvironmentAws == 'pci') {
              sh "echo 'pci'"
              ENV_ID = env.AWS_PCI_ID
            } else if ( params.EnvironmentAws == 'prod') {
              sh "echo 'prod'"
              ENV_ID = env.AWS_PROD_ID
            } else if ( params.EnvironmentAws == 'sand') {
              sh "echo 'sand'"
            } else if ( params.EnvironmentAws == 'hmlg') {
              sh "echo 'hmlg'"
            }
          }
          sh "cd /tmp/workspace/mockkid_ci-cd"
          sh "docker build -t ${ENV_ID}.dkr.ecr.us-east-1.amazonaws.com/ecr-${EnvironmentAws}-${App} ."
          sh "echo ${ENV_ID} > env"
        }
    }
    stage('Push Images') {
      steps {
          sh "ENV_ID=`cat env`"
          sh "aws ecr get-login --region us-east-1 --no-include-email |sh"
          sh "docker push ${ENV_ID}.dkr.ecr.us-east-1.amazonaws.com/ecr-${EnvironmentAws}-${App}"
        }
    }
    stage('Deploy') {
      steps {
        sh 'python deploy-bag.py'
      }
    }
  }
  triggers {
    pollSCM('H/3 * * * *')
   }
  }
