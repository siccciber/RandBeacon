package br.gov.inmetro.beacon.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public MessageConverter jsonMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }


    @Bean
    public MessageConverter messageConverter()
    {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        return new Jackson2JsonMessageConverter(mapper);
    }

//    @Bean
//    public AmqpTemplate rabbitTemplate();
//    RabbitTemplate template = new RabbitTemplate(connectionFactory());
//    RetryTemplate retryTemplate = new RetryTemplate();
//    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//		backOffPolicy.setInitialInterval(500);
//		backOffPolicy.setMultiplier(10.0);
//		backOffPolicy.setMaxInterval(10000);
//		retryTemplate.setBackOffPolicy(backOffPolicy);
//		template.setRetryTemplate(retryTemplate);
//		return template;
//    }

}
