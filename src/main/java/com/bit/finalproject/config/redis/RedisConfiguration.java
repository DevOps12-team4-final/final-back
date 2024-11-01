package com.bit.finalproject.config.redis;

import com.bit.finalproject.common.PageDeserializer;
import com.bit.finalproject.common.PageSerializer;
import com.bit.finalproject.dto.RoomChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
       objectMapper.registerModule(new PageJacksonModule());
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(Page.class, new PageSerializer()); // 수정된 부분
//        module.addDeserializer(Page.class, new PageDeserializer()); // 수정된 부분
//        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory());
       return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, RoomChatDto> chatRedisTemplate() {
        RedisTemplate<String,RoomChatDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        Jackson2JsonRedisSerializer<RoomChatDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper(),RoomChatDto.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }


}
