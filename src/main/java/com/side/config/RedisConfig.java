package com.side.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	public String host;
	@Value("${spring.data.redis.port}")
	public int port;
	@Value("${spring.data.redis.timeout}")
	private Long timeout;
	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		final SocketOptions socketoptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
		final ClientOptions clientoptions = ClientOptions.builder().socketOptions(socketoptions).build();
		LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder().clientOptions(clientoptions)
				.commandTimeout(Duration.ofMinutes(1))
				.shutdownTimeout(Duration.ZERO)
				.build();
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
		redisStandaloneConfiguration.setDatabase(0);
		return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
	}
	@Bean(name="redisTemplate")
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
