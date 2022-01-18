package com.project.backend.Controller;

import com.project.backend.Service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.ws.context.MessageContext;
import generatedsoapclasses.*;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.io.IOException;
import java.util.List;

@Component
@Endpoint
public class SubscriberSoapAPI {

    @Autowired
    private SubscriberService subscriberService;

    public SubscriberSoapAPI(SubscriberService subscriberService){
        this.subscriberService = subscriberService;
    }

    static final Logger log = LoggerFactory.getLogger(SubscriberSoapAPI.class);

    @PayloadRoot(namespace="GeneratedSOAPClasses",localPart = "getSubscriberRequest")
    @ResponsePayload
    public GetSubscriberResponse getSubscriberRequest(@RequestPayload GetSubscriberRequest request, MessageContext messageContext) throws IOException, ParseException {
        GetSubscriberResponse response = new GetSubscriberResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Get Subscriber with id = " + request.getId());
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            com.project.backend.Entity.Subscriber sub = subscriberService.getSubscriber(request.getId(), dbEnabled);
            generatedsoapclasses.Subscriber subSOAP = sub.convertSOAPModel();
            response.setSubscriber(subSOAP);
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Get Subscriber With ID " + request.getId() + " Not Found");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }


    @PayloadRoot(namespace="GeneratedSOAPClasses",localPart = "getSubscribersRequest")
    @ResponsePayload
    public GetSubscribersResponse getSubscribersRequest(MessageContext messageContext)throws IOException, ParseException {
        GetSubscribersResponse response = new GetSubscribersResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Get Subscribers");
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            List<com.project.backend.Entity.Subscriber> subsList = subscriberService.getSubscribers(dbEnabled);
            for(int i = 0; i < subsList.size(); i++)
            {
                generatedsoapclasses.Subscriber subSOAP = subsList.get(i).convertSOAPModel();
                response.getSubscribers().add(subSOAP);
            }
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Get All Subscribers Have an Internal Error");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }


    @PayloadRoot(namespace="GeneratedSOAPClasses",localPart = "getActiveSubscribersRequest")
    @ResponsePayload
    public GetSubscribersResponse getActiveSubscribersRequest(MessageContext messageContext)  throws IOException, ParseException {
        GetSubscribersResponse response = new GetSubscribersResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Get Active Subscribers");
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            List<com.project.backend.Entity.Subscriber> subsList = subscriberService.getActiveSubscribers(dbEnabled);
            for(int i = 0; i < subsList.size(); i++) {
                generatedsoapclasses.Subscriber subSOAP = subsList.get(i).convertSOAPModel();
                response.getSubscribers().add(subSOAP);
            }
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Get All Active Subscribers Have an Internal Error");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }


    @PayloadRoot(namespace = "GeneratedSOAPClasses", localPart = "addSubscriberRequest")
    @ResponsePayload
    public AddSubscriberResponse addSubscriber(@RequestPayload AddSubscriberRequest request, MessageContext messageContext) throws IOException, ParseException {
        AddSubscriberResponse response = new AddSubscriberResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Add Subscriber");
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            com.project.backend.Entity.Subscriber subscriber = new com.project.backend.Entity.Subscriber();
            subscriber.setName(request.getName());
            subscriber.setStatus(request.getStatus());
            subscriber.setMsisdn(request.getMsisdn());
            com.project.backend.Entity.Subscriber savedSubscriber = subscriberService.createSubscriber(subscriber, dbEnabled);
            generatedsoapclasses.Subscriber subSOAP = new generatedsoapclasses.Subscriber();
            BeanUtils.copyProperties(savedSubscriber, subSOAP);
            //generatedsoapclasses.Subscriber subSOAP = savedSubscriber.convertSOAPModel();
            response.setSubscriber(subSOAP);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Added Successfully");
            response.setServiceStatus(serviceStatus);
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Add Subscriber Have an Internal Error");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }


    @PayloadRoot(namespace = "GeneratedSOAPClasses", localPart = "updateSubscriberRequest")
    @ResponsePayload
    public UpdateSubscriberResponse updateSubscriber(@RequestPayload UpdateSubscriberRequest request, MessageContext messageContext) throws IOException, ParseException {
        UpdateSubscriberResponse response = new UpdateSubscriberResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Update Subscriber with id = " + request.getId());
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            com.project.backend.Entity.Subscriber subscriber = new com.project.backend.Entity.Subscriber();
            BeanUtils.copyProperties(request.getSubscriber(), subscriber);
            com.project.backend.Entity.Subscriber updatedSubscriber = subscriberService.updateSubscriber(subscriber, request.getId(), dbEnabled);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Updated Successfully");
            response.setServiceStatus(serviceStatus);
            generatedsoapclasses.Subscriber subSOAP = new generatedsoapclasses.Subscriber();
            BeanUtils.copyProperties(updatedSubscriber, subSOAP);
            response.setSubscriber(subSOAP);
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Update Subscriber With ID: " + request.getId() + " Have an Internal Error");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }

    @PayloadRoot(namespace = "GeneratedSOAPClasses", localPart = "deleteSubscriberRequest")
    @ResponsePayload
    public DeleteSubscriberResponse deleteSubscriber(@RequestPayload DeleteSubscriberRequest request, MessageContext messageContext) throws IOException, ParseException {
        DeleteSubscriberResponse response = new DeleteSubscriberResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        try{
            log.info("SOAP API Delete Subscriber with id = " + request.getId());
            boolean dbEnabled =  (Boolean)messageContext.getProperty("isDatabaseEnabled");
            com.project.backend.Entity.Subscriber subscriber = subscriberService.getSubscriber(request.getId(), dbEnabled);
            subscriberService.deleteOneSubscriber(request.getId(), dbEnabled);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
            response.setServiceStatus(serviceStatus);
            return response;
        }catch (Exception e){
            serviceStatus.setStatusCode(e.getMessage());
            serviceStatus.setMessage("SOAP API Delete Subscriber With ID: " + request.getId() + " Have an Internal Error");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }
}