pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }

        stage('Git') {
             steps {
                git 'https://github.com/rafisce/eyes.git'
             }
        }

        stage('Run Tests') {
             steps {
                  
                         sh './gradlew --no-daemon --debug :app:connectedDevDebugAndroidTest'
             }
        }


    }



}