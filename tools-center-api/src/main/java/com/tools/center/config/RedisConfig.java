//package com.tools.center.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
//    private static final GenericJackson2JsonRedisSerializer JACKSON__SERIALIZER = new GenericJackson2JsonRedisSerializer();
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(factory);
//        redisTemplate.setKeySerializer(STRING_SERIALIZER);
//        redisTemplate.setValueSerializer(JACKSON__SERIALIZER);
//        redisTemplate.setHashKeySerializer(STRING_SERIALIZER);
//        redisTemplate.setHashValueSerializer(JACKSON__SERIALIZER);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//}
