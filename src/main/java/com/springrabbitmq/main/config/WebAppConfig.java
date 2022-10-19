package com.springrabbitmq.main.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;

import com.springrabbitmq.main.Receiver;



public class WebAppConfig {
    /*this is the bean container that we'll use to contain
     * the constructor to create the receiver
    */


    static final String topicExchangeName = "unique-topic";
  
    static final String queueName = "queue-a";

    static final String routing_key="routing_pretty_key";

    /*We are creating a new QUEUE WITH IT'S CORRESPONDENT NAME.
     * The queue is the data structure that will help us
     * to save the temporary number of long-lasting
     * request that will be handled one by one
     * by the server
    */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /*It creates the exchange that is responsable for routing the 
     * messages to it's correspondent topic with help
     * of the bindings, in this case the routing keys
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /*Here we are declaring the only binding for now in this exercise,
     * so the queue-a is going to be exchanging the request with the unique-topic routing
     * that have the routing key "routing_pretty_key"
    */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing_key);
    }

    /*Setting the connection properties and the method name in the receiver class
     * to handle the messages.
     */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
