node {
   //git凭证id
   def git_auth = "7a2a6e76-49ec-4ec5-8c59-5b2284e27ee0"
   //git的url地址
   def git_url = "git@github.com:szyanghm/tools-center-cloud.git"
   //阿里云docker镜像仓库凭证id
   def aliyun_auth = "927ca715-da19-449c-8bd1-6c2c4b241411"
   //阿里云docker镜像仓库地址
   def aliyun_registry_url = "registry.cn-shenzhen.aliyuncs.com"
   //阿里云docker镜像仓库命名空间
   def aliyun_registry_namespace = "my_docker-repo"
   //获取当前选择的项目名称
   def selectedProjectNames = "${project_name}".split(",")
   //获取当前选择的服务器名称
   def selectedServers = "${publish_server}".split(",")
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
		 def project_name = "${projectInfo}".split("@")[0]
		 def port = "${projectInfo}".split("@")[1]
		 	  //构建镜像
	     //sh "mvn -f ${project_name} clean deploy -Dmaven.deploy.skip=true"
	     sh "mvn -f ${project_name} clean package dockerfile:build"
	     //通过jenkins凭证来配置docker镜像仓库账户密码
	     withCredentials([usernamePassword(credentialsId: "${aliyun_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
	        //登录到阿里云镜像仓库
	        sh "docker login --username=${username} --password=${password} ${aliyun_registry_url}"
		    sh "docker tag ${project_name} ${aliyun_registry_url}/${aliyun_registry_namespace}/${project_name}"
		    sh "docker push ${aliyun_registry_url}/${aliyun_registry_namespace}/${project_name}"
		    sh "echo 镜像上传成功!"
	     }
		 //遍历所有服务器，分别进行部署
		 for(int j=0;j<selectedServers.length;j++){
			def serverName = selectedServers[j]
			sshPublisher(publishers: [sshPublisherDesc(configName: "${serverName}", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/data/deploy.sh $aliyun_registry_url $aliyun_registry_namespace $project_name $port $username $password", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
		 }
	  }

   }
   stage('删除none旧版本docker镜像') {
      sh "docker images --quiet --filter=dangling=true | xargs --no-run-if-empty docker rmi"
   }

}