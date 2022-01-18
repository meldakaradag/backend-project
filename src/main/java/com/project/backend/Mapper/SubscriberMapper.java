package com.project.backend.Mapper;

import com.project.backend.DTO.SubscriberDTO;
import com.project.backend.Entity.Subscriber;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriberMapper {
    public SubscriberDTO entityToDto(Subscriber subscriber) {
        ModelMapper mapper =new ModelMapper();
        SubscriberDTO map = mapper.map(subscriber, SubscriberDTO.class);
        return map;
    }

    public List<SubscriberDTO> entityToDto(List<Subscriber> student) {
        return	student.stream().map(x -> entityToDto(x)).collect(Collectors.toList());

    }

    public Subscriber dtoToEntity(SubscriberDTO dto) {
        ModelMapper mapper = new ModelMapper();
        Subscriber map = mapper.map(dto, Subscriber.class);
        return map;
    }

    public List<Subscriber> dtoToEntity(List<SubscriberDTO> dto) {
        return dto.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }
}
