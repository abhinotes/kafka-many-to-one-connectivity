package com.abhinotes.m2o.connector.source.config;

import com.abhinotes.m2o.connector.source.parser.MessageParsingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;


@Configuration
public class RegisterListeners implements JmsListenerConfigurer {

    @Autowired
    JMSRegistryHelper registryHelper;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        MessageParsingHelper.prop.forEach((key, value)->
                registrar.registerEndpoint(registryHelper.getEndPoint((String)key)));
    }

}