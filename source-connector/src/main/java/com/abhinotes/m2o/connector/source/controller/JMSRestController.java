package com.abhinotes.m2o.connector.source.controller;

import com.abhinotes.m2o.connector.source.config.JMSRegistryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/jmsgateway")
public class JMSRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSRestController.class);

    @Autowired
    JMSRegistryHelper registryHelper;

    @GetMapping(value = "/list")
    public @ResponseBody
    Set<String> getAllJMSSourceListeners() {
        return registryHelper.jmsRegistry().getListenerContainerIds();
    }

    @GetMapping(value = "/stop")
    public String stopJMS(@RequestParam(value = "queue", required = true) String queue) {
        String response = "Succesfully Stopped JMS Listener on queue "+queue;
        try {
            registryHelper.jmsListenerContainer(queue).stop();
            if(registryHelper.jmsListenerContainer(queue).isRunning()) {
                response = response.concat(", Status : Running");
            } else {
                response = response.concat(", Status : Stopped");
            }

        } catch (Exception e) {
            String errorText = String.format("Unable to stop queue %s , action failed with error %s", queue,e.getMessage());
            LOGGER.error(errorText,e);
            response = errorText;
        }
        return response;
    }

    @GetMapping(value = "/start")
    public String startJMS(@RequestParam(value = "queue", required = true) String queue) {
        String response = "Succesfully Started JMS Listener on queue "+queue;
        try {
            registryHelper.jmsListenerContainer(queue).start();
        } catch (Exception e) {
            String errorText = String.format("Unable to start Listener on queue %s , action failed with error %s", queue,e.getMessage());
            LOGGER.error(errorText,e);
            response = errorText;
        }
        return response;
    }

    @GetMapping(value = "/register")
    public String registerJMS(@RequestParam(value = "queue", required = true) String queue) {
        String response = "Succesfully Started JMS Listener on queue "+queue;
        try {
            registryHelper.jmsListenerContainer(queue);
        } catch (Exception e) {
            String errorText = String.format("Unable to start Listener on queue %s , action failed with error %s", queue,e.getMessage());
            LOGGER.error(errorText,e);
            response = errorText;
        }
        return response;
    }

    @GetMapping(value = "/status")
    public String getJMSStatus(@RequestParam(value = "queue", required = true) String queue) {
        String response = "JMS Queue : "+queue;
        try {

            if(registryHelper.jmsListenerContainer(queue).isRunning()) {
                response = response.concat(", Status : Running");
            } else {
                response = response.concat(", Status : Stopped");
            }

        } catch (Exception e) {
            String errorText = String.format("Unable to get queue %s status, action failed with error %s", queue,e.getMessage());
            LOGGER.error(errorText,e);
            response = errorText;
        }
        return response;
    }

}
