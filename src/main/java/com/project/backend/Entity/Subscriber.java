package com.project.backend.Entity;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, length = 75)
    private String name;

    @Column(name = "msisdn", nullable = false, length = 20)
    private String msisdn;

    @Column(name = "status", nullable = false, length = 15)
    private String status;

    public generatedsoapclasses.Subscriber convertSOAPModel() {

        generatedsoapclasses.Subscriber soapSubscriber = new generatedsoapclasses.Subscriber();

        soapSubscriber.setId(this.id);
        soapSubscriber.setName(this.name);
        soapSubscriber.setMsisdn(this.msisdn);
        soapSubscriber.setStatus(this.status);

        return soapSubscriber;
    }
}
