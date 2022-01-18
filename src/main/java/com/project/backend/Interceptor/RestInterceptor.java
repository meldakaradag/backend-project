package com.project.backend.Interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//Interceptor class for Rest API
@Component
public class RestInterceptor implements HandlerInterceptor {

    static final Logger log = LoggerFactory.getLogger(RestInterceptor.class);

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
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("Rest API Prehandle Interceptor Get the isDatabaseEnabled value from application.proporties");
        String value = getPropertyValue("app.isDatabaseEnabled");
        boolean isDatabaseEnabled = Boolean.parseBoolean(value);
        request.setAttribute("isDatabaseEnabled", isDatabaseEnabled);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception) throws Exception {
    }
}
