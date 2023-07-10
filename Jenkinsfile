pipeline {
  agent any
  stages {
    stage('Make Environment') {
      parallel {
        stage('Touch local.properties') {
          steps {
            sh 'echo "sdk.dir=/opt/android-sdk-linux" >> local.properties'
          }
        }
        stage('Touch google-services.json') {
          steps {
            sh 'echo $GOOGLE_SERVICES_JSON | base64 --decode --ignore-garbage > demo/google-services.json'
          }
        }
        stage('Display directory') {
          steps {
            sh 'ls -la'
          }
        }
      }
    }
    stage('assembleDebug') {
      steps {
        sh './gradlew --no-daemon assembleDebug --stacktrace'
      }
    }
  }
  environment {
    GOOGLE_SERVICES_JSON = ''
  }
}
