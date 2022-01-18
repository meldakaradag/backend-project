package com.project.backend.Interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import javax.annotation.PostConstruct;

//Interceptor class for SOAP API
@Component
public class CustomEndpointInterceptor implements EndpointInterceptor {

    private static final Log LOG = LogFactory.getLog(CustomEndpointInterceptor.class);

    @Autowired
    private Environment env;

    private static Environment environment;

    @PostConstruct
    public void init(){
        environment = env;
        System.out.println(environment == env);
    }

    //checks application.proporties file to see if database or data.json file is used
    public String getPropertyValue(String key) {
        String returnValue = "No data property value";
        try{
            String keyValue = environment.getProperty(key);
            if( keyValue!= null && !keyValue.isEmpty())
            { returnValue = keyValue;}
            return returnValue;
        }catch (Exception e){
            return returnValue;
        }
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        LOG.info("SOAP API Endpoint Request Handling Get the isDatabaseEnabled value from application.proporties");
        String value = getPropertyValue("app.isDatabaseEnabled");
        boolean isDatabaseEnabled = Boolean.parseBoolean(value);
        messageContext.setProperty("isDatabaseEnabled", isDatabaseEnabled);
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
    }
}