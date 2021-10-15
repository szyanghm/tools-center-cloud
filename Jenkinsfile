node {
   //git凭证id
   def git_auth = "7a2a6e76-49ec-4ec5-8c59-5b2284e27ee0"
   //git的url地址
   def git_url = "git@github.com:szyanghm/tools-center-cloud.git"
   def aliyun_auth = "927ca715-da19-449c-8bd1-6c2c4b241411"
   def aliyun_registry_url = "registry.cn-shenzhen.aliyuncs.com"
   def aliyun_registry_namespace = "my_docker-repo"
   def selectedProjectNames = "${project_name}".split(",")
   stage('拉取代码') {
      checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
   }
   stage('编译，安装公共子工程') {
      sh "mvn -f tools-center-common clean install"
   }
   //stage('编译，安装feign工程') {
      //sh "mvn -f tools-center-contract clean install"
   //}
   stage('编译，打包微服务工程') {
      for(int i=0;i<selectedProjectNames.length;i++){
	     def projectInfo = selectedProjectNames[i];
		 def currentProjectName = "${projectInfo}".split("@")[0]
		 def currentProjectPort = "${projectInfo}".split("@")[1]
		 	  //构建镜像
	     //sh "mvn -f ${currentProjectName} clean deploy -Dmaven.deploy.skip=true"
	     sh "mvn -f ${currentProjectName} clean package dockerfile:build"
	     //通过jenkins凭证来配置docker镜像仓库账户密码
	     withCredentials([usernamePassword(credentialsId: "${aliyun_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
	        //登录到阿里云镜像仓库
	        sh "docker login --username=${username} --password=${password} ${aliyun_registry_url}"
		    sh "docker tag ${currentProjectName} ${aliyun_registry_url}/${aliyun_registry_namespace}/${currentProjectName}"
		    sh "docker push ${aliyun_registry_url}/${aliyun_registry_namespace}/${currentProjectName}"
		    sh "echo 镜像上传成功"
	     }
	  }

   }
   stage('停止，删除旧容器,启动镜像容器') {
	  //sh "docker stop ${project_name}"
	  //sh "docker rm ${project_name}"
	 
	  //sh "docker run --name ${project_name} -d ${project_name}:latest"
   }
   stage('删除none旧版本docker镜像') {
      sh "docker images --quiet --filter=dangling=true | xargs --no-run-if-empty docker rmi"
   }

}