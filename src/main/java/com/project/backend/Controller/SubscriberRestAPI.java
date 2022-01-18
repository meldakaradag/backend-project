package com.project.backend.Controller;

import com.project.backend.DTO.SubscriberDTO;
import com.project.backend.Entity.Subscriber;
import com.project.backend.Mapper.SubscriberMapper;
import com.project.backend.Service.SubscriberService;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/subscriber")
public class SubscriberRestAPI {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    SubscriberMapper mapper;

    static final Logger log = LoggerFactory.getLogger(SubscriberRestAPI.class);

    @Scheduled(cron = "*/30 * * * * *")
    @Caching(evict = {@CacheEvict(cacheNames = "Subscribers", allEntries = true),
            @CacheEvict(cacheNames = "ActiveSubscribers", allEntries = true)})
    public void evictAllCacheOnScheduleJob (){
        log.info("Scheduled job ran to clean all caches.");
    }

    @GetMapping("")
    ResponseEntity<List<SubscriberDTO>> getAll(@RequestAttribute("isDatabaseEnabled") Boolean dbEnable) throws IOException, ParseException {
        try{
            log.info("Rest API Get Subscribers");
            List<Subscriber> entityList = subscriberService.getSubscribers(dbEnable);
            return new ResponseEntity<List<SubscriberDTO>>(mapper.entityToDto(entityList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active")
    ResponseEntity<List<SubscriberDTO>> getActive(@RequestAttribute("isDatabaseEnabled") Boolean dbEnable) throws IOException, ParseException {
        try{
            log.info("Rest API Get Active Subscribers");
            List<Subscriber> entityList = subscriberService.getActiveSubscribers(dbEnable);
            return new ResponseEntity<List<SubscriberDTO>>(mapper.entityToDto(entityList), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<SubscriberDTO> getOne(@PathVariable long id, @RequestAttribute("isDatabaseEnabled") Boolean dbEnable) throws IOException, ParseException  {
        try{
            log.info("Rest API Get Subscriber with id = " + id);
            Subscriber entity = subscriberService.getSubscriber(id, dbEnable);
            return new ResponseEntity<SubscriberDTO>(mapper.entityToDto(entity), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    ResponseEntity<SubscriberDTO> createSubscriber(@RequestBody SubscriberDTO dto, @RequestAttribute("isDatabaseEnabled") Boolean dbEnable)  throws IOException, ParseException{
        try{
            log.info("Rest API Add Subscriber");
            Subscriber newSubscriber = mapper.dtoToEntity(dto);
            Subscriber entity = subscriberService.createSubscriber(newSubscriber, dbEnable);
            return new ResponseEntity<SubscriberDTO>(mapper.entityToDto(entity), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<SubscriberDTO> updateSubscriber(@RequestBody SubscriberDTO dto, @PathVariable long id, @RequestAttribute("isDatabaseEnabled") Boolean dbEnable) throws IOException, ParseException{
        try{
            log.info("Rest API Update Subscriber with id = " + id);
            Subscriber newSubscriber = mapper.dtoToEntity(dto);
            Subscriber entity = subscriberService.updateSubscriber(newSubscriber, id, dbEnable);
            return new ResponseEntity<SubscriberDTO>(mapper.entityToDto(entity), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteOneSubscriber(@PathVariable long id, @RequestAttribute("isDatabaseEnabled") Boolean dbEnable) throws IOException, ParseException {
        try{
            log.info("Rest API Delete Subscriber with id = " + id);
            subscriberService.deleteOneSubscriber(id, dbEnable);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
