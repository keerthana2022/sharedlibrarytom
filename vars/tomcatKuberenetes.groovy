def test( String dregistryin = 'a', String docTag = 'a', String grepo = 'a', String gbranch = 'a') {

pipeline {
environment { 
		registry = "$dregistryin" 	
		dockerTag = "${docTag}$BUILD_NUMBER"
		gitRepo = "${grepo}"
		gitBranch = "${gbranch}"
	}
	
	agent none 
	 stages {
		stage("Checkout"){
      agent{label 'docker'}
			steps {
			
			    checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/keerthana2022/newtestrepo.git']]])
				 //checkout([$class: 'GitSCM', branches: [[name: "$gitBranh"]], extensions: [], userRemoteConfigs: [[credentialsId: "$gitCredId", url: "$gitRepo"]]])
			}
		}
		stage("Build"){
      agent{label 'docker1'}
			steps {
			
			    sh 'docker build -t "$registry:$dockerTag" .'
			}
		}
	 }
	
    }
}
