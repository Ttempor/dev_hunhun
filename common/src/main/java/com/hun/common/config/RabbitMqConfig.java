package com.hun.common.config;


import com.hun.common.constants.MQ;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    // 声明队列
    @Bean("GradeStartQueue")
    public Queue startQueue() {
        return new Queue(MQ.START_GRADE_QUEUE, true, false, false);
    }

    // 声明自定义交换机
    @Bean("GradeStartExchange")
    public CustomExchange gradeStartExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MQ.START_GRADE_EXCHANGE, "x-delayed-message", true, false,  args);
    }

    // 声明队列和延迟交换机的绑定
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("GradeStartQueue")Queue delayedQueue,
                                       @Qualifier("GradeStartExchange")CustomExchange exchange) {
        return BindingBuilder.bind(delayedQueue).to(exchange).with(MQ.START_GRADE_ROUTER_KEY).noargs();
    }


    // 声明队列
    @Bean("GradeEndQueue")
    public Queue endQueue() {
        return new Queue(MQ.END_GRADE_QUEUE,true,false,false);
    }

    @Bean("GradeEndExchange")
    public CustomExchange endExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MQ.END_GRADE_EXCHANGE, "x-delayed-message", true, false,  args);
    }

    @Bean
    public Binding e(@Qualifier("GradeEndQueue")Queue delayedQueue,
                     @Qualifier("GradeEndExchange")CustomExchange exchange)  {
        return BindingBuilder.bind(delayedQueue).to(exchange).with(MQ.END_GRADE_ROUTER_KEY).noargs();
    }
}
