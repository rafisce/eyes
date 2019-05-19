pipeline {
    agent { docker { 
        image 'anthonymonori/android-ci-image'
        //arugment to execute everything as a sudoer
        args '-u root:sudo'

        }
    }
    stages {
    stage('Git') {
      // Get some code  from a GitHub repository
      steps{
          git 'https://github.com/rafisce/eyes.git'
      }
        stage('SonarQube analysis') {
    // requires SonarQube Scanner 2.8+
    def scannerHome = tool 'SonarQube Scanner 2.8';
    withSonarQubeEnv('My SonarQube Server') {
      sh "${scannerHome}/bin/sonar-scanner"
    }
  }
   }
 
    stage('Run Tests'){
          steps{
               sh """
               yes | sdkmanager --licenses
                ./gradlew test
                """
          }
    }
    
    stage('Publish Test Results'){
        steps{
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'app/build/reports/tests/testReleaseUnitTest', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
        }
    }
    
}
}
