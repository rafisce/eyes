pipeline {
    agent { docker { image 'maven:3.3.3' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }

    stages {
            stage('Git') {
                steps {
                    git 'https://github.com/rafisce/eyes.git'
                }
            }
        }

}