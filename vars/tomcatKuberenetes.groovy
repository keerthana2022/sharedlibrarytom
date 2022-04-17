def call( String dregistryin = 'a', String docTag = 'a', String grepo = 'a', String gbranch = 'a') {

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
				
			}
		}
		stage("Build"){
      agent{label 'docker'}
			steps {
			
			    sh 'docker build -t "$registry:$dockerTag" .'
			}
		}
		 
		 
		 stage('PUSH HUB') { 
       agent{label 'docker'}
			 steps { 
				 sh 'docker push $registry:$dockerTag'
				 
                			
			} 
		}
	 
	 stage('DEPLOY IMAGE') {
      agent{label 'kubernetes'}
			steps {
				sh 'kubectl set image deployment/tomdeploy tomcontainer="$registry:$dockerTag" --record'
			}
		}
	 
	 
	 
	 }
	
	
    }
}
