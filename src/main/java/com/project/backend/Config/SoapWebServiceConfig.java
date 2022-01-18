package com.project.backend.Config;

import com.project.backend.Interceptor.CustomEndpointInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import java.util.List;

@EnableWs
@Configuration
public class SoapWebServiceConfig extends WsConfigurerAdapter {

    @Autowired
    private CustomEndpointInterceptor customEndpointInterceptor;

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/soapWS/*");
    }


    @Bean
    public XsdSchema subscriberSchema() {
        return new SimpleXsdSchema(new ClassPathResource("XSDFiles/Subscriber.xsd"));
    }

    @Bean
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema subscriberSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setSchema(subscriberSchema);
        definition.setLocationUri("/soapWS");
        definition.setPortTypeName("SubscriberServicePort");
        definition.setTargetNamespace("http://localhost:8080");
        return definition;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        // register global interceptor
        interceptors.add(customEndpointInterceptor);
    }


}
