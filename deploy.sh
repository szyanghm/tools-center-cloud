#! /bin/sh
#接收外部参数
aliyun_registry_url=$1
aliyun_registry_namespace=$2
project_name=$3
port=$4
username=$5
password=$6


imageName=$aliyun_registry_url/$aliyun_registry_namespace/$project_name

echo "$imageName"
#查询容器是否存在，存在则删除
containerId=`docker ps -a | grep -w ${project_name} | awk '{print $1}'`
if("$containerId" != ""); then
  #停到容器
  docker stop $containerId
  #删除容器
  docker rm $containerId
  echo "删除容器成功"
fi
#查询镜像是否存在，存在则删除
imageId=`docker images -a | grep -w ${project_name} | awk '{print $3}'`
if("$imageId" != ""); then
  #删除容器
  docker rmi -f $imageId
  echo "删除镜像成功"
fi
#登录阿里云docker镜像仓库
docker login --username=${username} --password=${password} ${aliyun_registry_url}
#下载镜像
docker pull $imageName
#启动容器
docker run -di -p $port:$port $imageName