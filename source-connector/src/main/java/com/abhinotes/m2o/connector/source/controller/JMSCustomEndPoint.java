package com.abhinotes.m2o.connector.source.controller;

import com.abhinotes.m2o.connector.source.config.JMSRegistryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Component
@RestControllerEndpoint(id = "m2o")
public class JMSCustomEndPoint {

    @Autowired
    JMSRegistryHelper registryHelper;

    @GetMapping("/registered-listeners")
    public @ResponseBody
    Set<String> getAllJMSSourceListeners() {
        return registryHelper.jmsRegistry().getListenerContainerIds();
    }

}
