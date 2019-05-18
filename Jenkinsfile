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
                  // Compile and run the unit tests for the app and its dependencies
                         sh './gradlew testDebugUnitTest testDebugUnitTest'

                         // Analyse the test results and update the build result as appropriate
                         junit '**/TEST-*.xml'
             }
        }


    }



}