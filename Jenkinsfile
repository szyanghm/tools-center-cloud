node {
   //git凭证id
   def git_auth = "7a2a6e76-49ec-4ec5-8c59-5b2284e27ee0"
   //git的url地址
   def git_url = "git@github.com:szyanghm/tools-center-cloud.git"
   def imageNone = "dangling=true"
   stage('拉取代码') {
      checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
   }
   stage('编译，安装公共子工程') {
      sh "mvn -f tools-center-common clean install"
   }
   stage('编译，安装feign工程') {
      sh "mvn -f tools-center-contract clean install"
   }
   stage('编译，打包微服务工程') {
      docker rmi /$(docker images -f "${imageNone}" -q)
      sh "mvn -f ${project_name} clean deploy -Dmaven.deploy.skip=true"
   }

}