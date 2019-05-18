#!/usr/bin/groovy

/*
 * Copyright (c) 2016, Andrey Makeev <amaksoft@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice unmodified, this list of conditions, and the following
 *    disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and|or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// Env variables for git push
env.J_USERNAME = "Jenkins CI"
env.J_EMAIL = "jenkins-ci@example.com"
env.J_GIT_CONFIG = "true"
// Use credentials id from Jenkins (Does anyone know a way to reference them by name rather than by id?)
env.J_CREDS_IDS = '02aa92ec-593e-4a90-ac85-3f43a06cfae3'

def gitLib

timestampedNode ("AndroidBuilder") {
  stage ("Checkout") {
    checkout scm
    sh "chmod a+x ./gradlew"
    gitLib = load "git_push_ssh.groovy"
  }
  
  stage ("Build") {
    // Check environment (We define ANDROID_HOME in node settings)
    if (env.ANDROID_HOME == null || env.ANDROID_HOME == "") error "ANDROID_HOME not defined"
    if (env.JAVA_HOME == null || env.JAVA_HOME == "") error "JAVA_HOME not defined"

    // Default parameters (In case file is unreadable or missing)
    def d = [versionName: 'unversioned', versionCode: '1']
    // Read properties from file (Right now we only keep versionName and VersionCode there)
    HashMap<String, Object> props = readProperties defaults: d, file: 'gradle.properties'
    // Optional user input to override parameters
    def userInput
    try {
      timeout(time: 60, unit: 'SECONDS') {
        userInput = input( id:'userInput', message: 'Override build parameters?', parameters: [
          string(defaultValue: props.versionName, description: 'App version (without build number)', name: 'versionName'),
          string(defaultValue: props.versionCode, description: 'Version code (for GooglePlay Store)', name: 'versionCode')
        ])
        logOverrides(userInput, props, "manual_override.log")
        props.putAll(userInput)
        echo("Parameters entered : ${userInput.toString()}")
      }
    } catch (Exception e) {
      echo "User input timed out or cancelled, continue with default values"
    }
    // Change build name to current app version
    currentBuild.displayName = "${props.versionName}.${env.BUILD_NUMBER}"
    // Common build arguments
    env.COMMON_BUILD_ARGS = " -PBUILD_NUMBER=${env.BUILD_NUMBER} -PBRANCH_NAME=${env.BRANCH_NAME}" +
      " -PversionName=${props.versionName} -PversionCode=${props.versionCode}"

    // Build the app
    sh "./gradlew clean"
        sh """./gradlew assembleDebug ${env.COMMON_BUILD_ARGS}
              ./gradlew assembleRelease ${env.COMMON_BUILD_ARGS}
           """
    }

    stage('Save artifacts and publish') {
      // Save build results
      step([$class: 'ArtifactArchiver', artifacts: "**/*.apk", excludes: "**/*unaligned.apk", fingerprint: true])
      // Push changes and tag
      gitLib.pushSSH(commitMsg: "Jenkins build #${env.BUILD_NUMBER} from ${env.BRANCH_NAME}", 
        tagName: "build/${env.BRANCH_NAME}/${env.BUILD_NUMBER}", files: ".", config: true);
      sendEmails();
    }

  stage ('Crashlytics register') {
    sh """./gradlew crashlyticsUploadDistributionDebug ${env.COMMON_BUILD_ARGS}
          ./gradlew crashlyticsUploadDistributionRelease ${env.COMMON_BUILD_ARGS}
       """
  }
}

stage ('Release') {
  try {
    input 'Do we release this build?'
    node {
      echo "Push Release tag"
      def date  = sh(returnStdout: true, script: 'date -u +%Y%m%d').trim()// = new Date().format('yyyyMMdd') // apparently we can't use Date here, not a problem
      gitLib.pushSSH(tagName: "release-${date}", commitMsg: "Jenkins promoted");
      // Do your release stuff
    }
  } catch (Exception e) {
    echo "Release cancelled"
  }
}

// To send emails to everyone relevant to this build (Requires Email-ext plugin)
def sendEmails() {
  emailext body: "See ${env.BUILD_URL}",
    recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
      subject: "Jenkins Build Successful",
      to: "admin@example.com";
}

// To log manual overrides
@NonCPS
def logOverrides(def ov_map, def orig_map, def filename) {
  def header = "# Build ${env.BUILD_NUMBER}-${env.BRANCH_NAME} manual parameters override: ";
  def headWritten = false;
  ov_map.each{ k, v ->
    if( orig_map[k] != v ) {
      if (!headWritten) {
        sh "echo \"${header}\" >> ${filename}"; // apparently we are not allowed to use File.write() in this DSL
        headWritten = true;
      };
      sh "echo \"${k}=${v}\" >> ${filename}"
    }
  }
}

// Taken from jenkinsci/jenkins project (https://github.com/jenkinsci/jenkins/blob/master/Jenkinsfile)
// to add timestamps to logs
def timestampedNode(String label = "master", Closure body) {
  node(label) {
    wrap([$class: 'TimestamperBuildWrapper']) {
      body.call();
    }
  }
}
