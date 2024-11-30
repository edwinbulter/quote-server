pipeline {
    agent any

    stages {

        stage('Build snapshot') {
            steps {
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }

        stage('Nexus deployment') {
            steps{
                sh 'mvn -B -DskipTests=true deploy'
                stash includes: 'target/*.jar', name: 'quote-jar'
            }
        }

        stage('Stop service') {
            steps {
                sh 'systemctl stop quote'
           }
        }

        stage('Copy Jar') {
            steps {
                unstash 'quote-jar'
                sh 'cp *.jar /opt/quote/'
            }
        }

        stage('Start service') {
            steps {
                sh 'systemctl start quote'
           }
        }
    }
}