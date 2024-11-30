pipeline {
    agent any

    stages {

        stage('Build snapshot') {
            steps {
                echo 'Build quote-server using Maven'
                sh 'mvn clean install'
            }
        }

        stage('Nexus deployment') {
            steps{
                echo 'Deploy quote-server to Nexus using Maven'
                sh 'mvn -B -DskipTests=true deploy'
                stash includes: 'target/*.jar', name: 'quote-jar'
            }
        }

        stage('Stop service') {
            steps {
                echo 'Stop quote-server systemd service'
                sh 'systemctl stop quote'
           }
        }

        stage('Copy Jar') {
            steps {
                echo 'Copy quote-server jar to /opt/quote'
                unstash 'quote-jar'
                sh 'cp *.jar /opt/quote/'
            }
        }

        stage('Start service') {
            steps {
                echo 'Start quote-server systemd service'
                sh 'systemctl start quote'
           }
        }
    }
}