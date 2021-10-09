package com.tools.center;

import com.tools.center.common.Contents;
import com.tools.center.service.core.HappyChatServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class NettyApplication {

	public static void main(String[] args) {
		final HappyChatServer server = new HappyChatServer(Contents.DEFAULT_PORT);
		server.init();
		server.start();
		// 注册进程钩子，在JVM进程关闭前释放资源
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				server.shutdown();
				log.warn(">>>>>>>>>> jvm shutdown");
				System.exit(0);
			}
		});
		SpringApplication.run(NettyApplication.class, args);
	}

}
