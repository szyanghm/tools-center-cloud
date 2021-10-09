node {
   //git凭证id
   def git_auth = "f8d39dac-66a0-4702-9e17-c444fada241b"
   //git的url地址
   def git_url = "git@gitee.com:haimingyang/tools-center-cloud.git"
   stage('拉取代码') {
      checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "$git_url"]]])
   }
   stage('编译，安装公共子工程') {
      sh "mvn -f tools-center-common clean install"
   }
   stage('编译，安装feign工程') {
      sh "mvn -f tools-center-contract clean install"
   }
   stage('编译，打包微服务工程') {
      sh "mvn -f ${project_name} clean package dockerfile:build"
   }


}