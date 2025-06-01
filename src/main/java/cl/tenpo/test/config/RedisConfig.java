package cl.tenpo.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

/**
 * Configuration class for setting up Redis connection using Jedis.
 * <p>
 * This class defines the necessary configuration to connect to a Redis server
 * using the {@code JedisPooled} client. It reads the Redis host and port
 * from application properties and provides a {@code JedisPooled} bean for dependency injection.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Reads Redis connection details from application properties.</li>
 *   <li>Creates and configures a pooled Jedis client for efficient Redis operations.</li>
 * </ul>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@code spring.redis.host}: The Redis server hostname.</li>
 *   <li>{@code spring.redis.port}: The Redis server port.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>The {@code JedisPooled} bean is thread-safe and can be used across multiple threads.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private Integer port;

  @Bean
  public JedisPooled jedisPooled() {
    return new JedisPooled(host, port);
  }
}