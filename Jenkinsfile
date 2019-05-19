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
    withSonarQubeEnv('My SonarQube Server') {
      // requires SonarQube Scanner for Gradle 2.1+
      // It's important to add --info because of SONARJNKNS-281
      sh './gradlew --info sonarqube'
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
