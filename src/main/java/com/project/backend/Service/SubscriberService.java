package com.project.backend.Service;

import com.project.backend.Entity.Subscriber;
import com.project.backend.Repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import javax.ws.rs.NotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Component
@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    static final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    @Cacheable(cacheNames = "Subscriber", key = "#id")
    public Subscriber getSubscriber(long id, boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Got Subscriber with " + id +" id from Database");
            return subscriberRepository.findById(id).orElseThrow(() -> new NotFoundException());
        }
        else{      //work with data.json file
            Subscriber response = new Subscriber();
            Object obj = new org.json.simple.parser.JSONParser().parse(new FileReader("src/main/resources/data.json"));
            JSONObject jo = (JSONObject) obj;
            JSONArray ja = (JSONArray) jo.get("subscribers");
            Iterator itr2 = ja.iterator();
            while (itr2.hasNext())
            {
                Iterator itr1  = ((Map) itr2.next()).entrySet().iterator();
                Subscriber sub = new Subscriber();
                while (itr1.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr1.next();
                    if (pair.getKey().equals("id"))
                        sub.setId((long)pair.getValue());
                    else if (pair.getKey().equals("name"))
                        sub.setName((String)pair.getValue());
                    else if (pair.getKey().equals("msisdn"))
                        sub.setMsisdn((String)pair.getValue());
                    else if (pair.getKey().equals("status"))
                        sub.setStatus((String)pair.getValue());
                }
                if(sub.getId() == id )
                    response = sub;

            }
            log.info("Service Got Subscriber with " + id +" id from data.json");
            if (response.getId() == id)
                return response;
            else
                throw new NotFoundException();
        }
    }

    @Cacheable(cacheNames = "Subscribers")
    public List<Subscriber> getSubscribers(boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Got Subscribers from Database");
            return subscriberRepository.findAll();
        }
        else{      //work with data.json file
            List<Subscriber> subscriberList = new ArrayList<Subscriber>();
            Object obj = new org.json.simple.parser.JSONParser().parse(new FileReader("src/main/resources/data.json"));
            JSONObject jo = (JSONObject) obj;
            JSONArray ja = (JSONArray) jo.get("subscribers");
            Iterator itr2 = ja.iterator();
            while (itr2.hasNext())
            {
                Iterator itr1  = ((Map) itr2.next()).entrySet().iterator();
                Subscriber response = new Subscriber();
                while (itr1.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr1.next();
                    if (pair.getKey().equals("id"))
                        response.setId((long)pair.getValue());
                    else if (pair.getKey().equals("name"))
                        response.setName((String)pair.getValue());
                    else if (pair.getKey().equals("msisdn"))
                        response.setMsisdn((String)pair.getValue());
                    else if (pair.getKey().equals("status"))
                        response.setStatus((String)pair.getValue());
                }
                subscriberList.add(response);

            }
            log.info("Service Got Subscribers from data.json");
            return subscriberList;
        }
    }

    @Cacheable(cacheNames = "ActiveSubscribers")
    public List<Subscriber> getActiveSubscribers(boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Got Active Subscribers from Database");
            return subscriberRepository.getActiveSubscribers();
        }
        else{      //work with data.json file
            List<Subscriber> activeSubscriberList = new ArrayList<Subscriber>();
            Object obj = new JSONParser().parse(new FileReader("src/main/resources/data.json"));
            JSONObject jo = (JSONObject) obj;
            JSONArray ja = (JSONArray) jo.get("subscribers");
            Iterator itr2 = ja.iterator();
            while (itr2.hasNext())
            {
                Iterator itr1  = ((Map) itr2.next()).entrySet().iterator();
                Subscriber response = new Subscriber();
                while (itr1.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr1.next();
                    if (pair.getKey().equals("id"))
                        response.setId((long)pair.getValue());
                    else if (pair.getKey().equals("name"))
                        response.setName((String)pair.getValue());
                    else if (pair.getKey().equals("msisdn"))
                        response.setMsisdn((String)pair.getValue());
                    else if (pair.getKey().equals("status"))
                        response.setStatus((String)pair.getValue());
                }
                if(response.getStatus().equals("ACTIVE"))
                    activeSubscriberList.add(response);
            }
            log.info("Service Got Active Subscribers from data.json");
            return activeSubscriberList;
        }

    }

    @CachePut(cacheNames = "Subscriber", key = "#result.id")
    public Subscriber createSubscriber(@RequestBody Subscriber newSubscriber, boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Created New Subscriber to Database");
            if(subscriberRepository.existsById(newSubscriber.getId()))
                throw new IllegalArgumentException("This subscriber is already exist.");
            else
                return subscriberRepository.save(newSubscriber);
        }
        else{      //work with data.json file
            List<Subscriber> subscriberList = getSubscribers(isDatabaseEnabled);
            Subscriber subscriberWithThisId = getSubscriber(newSubscriber.getId(),isDatabaseEnabled);
            if (subscriberWithThisId.getId() == newSubscriber.getId())
                throw new IllegalArgumentException("This subscriber is already exist.");
            else {
                subscriberList.add(newSubscriber);
                JSONArray array = new JSONArray();
                for (Subscriber subs : subscriberList)
                {
                    JSONObject obj = new JSONObject();
                    obj.put("id", subs.getId());
                    obj.put("name", subs.getName());
                    obj.put("msisdn", subs.getMsisdn());
                    obj.put("status", subs.getStatus());
                    array.add(obj);
                }
                JSONObject jo = new JSONObject();
                jo.put("subscribers", array);
                FileWriter pw = new FileWriter("src/main/resources/data.json");
                pw.write(jo.toJSONString());
                pw.flush();
                pw.close();
                log.info("Service Created Subscriber to data.json");
                return newSubscriber;
            }
        }
    }

    @CachePut(cacheNames = "Subscriber", key = "#result.id")
    public Subscriber updateSubscriber(@RequestBody Subscriber newSubscriber, @PathVariable long id, boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Updated Subscriber with " + id +" id in Database");
            return subscriberRepository.findById(id)
                    .map(Subscriber -> {
                        Subscriber.setName(newSubscriber.getName());
                        Subscriber.setMsisdn(newSubscriber.getMsisdn());
                        Subscriber.setStatus(newSubscriber.getStatus());
                        return subscriberRepository.save(Subscriber);
                    })
                    .orElseGet(() -> {
                        throw new IllegalArgumentException("This subscriber with "+ id +" is not exist.");
                        /*newSubscriber.setId(id);
                        return subscriberRepository.save(newSubscriber);*/
                    });
        }
        else{      //work with data.json file
            Subscriber subs = getSubscriber(id, isDatabaseEnabled);

            if (subs.getId() != newSubscriber.getId())
                throw new IllegalArgumentException("This subscriber with" + id +"id is not exist.");
            else {
                subs.setName(newSubscriber.getName());
                subs.setMsisdn(newSubscriber.getMsisdn());
                subs.setStatus(newSubscriber.getStatus());
                JSONArray array = new JSONArray();
                List<Subscriber> subscriberList = getSubscribers(isDatabaseEnabled);
                for (Subscriber sub : subscriberList)
                {
                    if (sub.getId() == id)
                        sub = subs;

                    JSONObject obj = new JSONObject();
                    obj.put("id", sub.getId());
                    obj.put("name", sub.getName());
                    obj.put("msisdn", sub.getMsisdn());
                    obj.put("status", sub.getStatus());
                    array.add(obj);
                }

                JSONObject jo = new JSONObject();
                jo.put("subscribers", array);

                FileWriter pw = new FileWriter("src/main/resources/data.json");
                pw.write(jo.toJSONString());
                pw.flush();
                pw.close();
                log.info("Service Updated Subscriber with " + id +" id in data.json");
                return newSubscriber;
            }
        }



    }

    @CacheEvict(value = "Subscriber",key="#id")
    public void deleteOneSubscriber(@PathVariable long id, boolean isDatabaseEnabled) throws IOException, ParseException {
        if (isDatabaseEnabled == true) //work with database
        {
            log.info("Service Deleted Subscriber with " + id +" id from Database");
            try{
                subscriberRepository.deleteById(id);
            }
            catch (Exception e){
                throw new NotFoundException(
                        "Subscriber with id " + id + " does not exists");
            }
        }
        else{      //work with data.json file
            JSONArray array = new JSONArray();
            List<Subscriber> subscriberList = getSubscribers(isDatabaseEnabled);
            for (Subscriber sub : subscriberList)
            {
                if (sub.getId() == id)
                {

                }
                else {
                    JSONObject obj = new JSONObject();
                    obj.put("id", sub.getId());
                    obj.put("name", sub.getName());
                    obj.put("msisdn", sub.getMsisdn());
                    obj.put("status", sub.getStatus());
                    array.add(obj);
                }
            }
            JSONObject jo = new JSONObject();
            jo.put("subscribers", array);
            FileWriter pw = new FileWriter("src/main/resources/data.json");
            pw.write(jo.toJSONString());
            pw.flush();
            pw.close();
            log.info("Service Deleted Subscriber with " + id +" id from data.json");
        }
    }

}
