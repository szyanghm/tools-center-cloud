node {
   //git凭证id
   def git_auth = "7a2a6e76-49ec-4ec5-8c59-5b2284e27ee0"
   //git的url地址
   def git_url = "git@github.com:szyanghm/tools-center-cloud.git"
   def imageNone = "dangling=true"
   def str = "\$(docker images -f ${imageNone} -q)"
   def projectName = "tools-center-service"
   //stage('拉取代码') {
      //checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
   //}
   //stage('编译，安装公共子工程') {
      //sh "mvn -f tools-center-common clean install"
   //}
   //stage('编译，安装feign工程') {
      //sh "mvn -f tools-center-contract clean install"
   //}
   //stage('编译，打包微服务工程') {
      //sh "mvn -f ${project_name} clean deploy -Dmaven.deploy.skip=true"
   //}
   stage('停止，删除旧容器,启动镜像容器') {
	  //sh "docker stop ${project_name}"
	  //sh "docker rm ${project_name}"
	  def prot = "8081"
	  if(${project_name}==${projectName}){
          prot = "18080"
      }
	  sh "docker run --name ${project_name} -p prot:prot -d ${project_name}:latest"
   }
   stage('删除none旧版本docker镜像') {
      sh "docker images --quiet --filter=dangling=true | xargs --no-run-if-empty docker rmi"
   }

}