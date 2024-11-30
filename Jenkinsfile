pipeline {
    agent any
    tools {
        maven 'Apache-maven-3.9.6'
    }
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
                withMaven(mavenSettingsConfig: '8268de39-819f-460c-a9e1-b5532d856176') {
                    sh 'mvn -B -DskipTests=true deploy -DrepositoryId=maven-snapshots'
                }
                stash includes: 'target/*.jar', name: 'quote-jar'
                echo 'Finished with Deploy quote-server to Nexus using Maven'
            }
        }

        stage('Stop service') {
            steps {
                echo 'Stop quote-server systemd service'
                sh 'sudo systemctl stop quote-server'
           }
        }

        stage('Copy Jar') {
            steps {
                echo 'Copy quote-server jar to /opt/quote-server'
                unstash 'quote-jar'
                sh 'cp target/*.jar /opt/quote-server/'
            }
        }

        stage('Start service') {
            steps {
                echo 'Start quote-server systemd service'
                sh 'sudo systemctl start quote-server'
           }
        }
    }
}