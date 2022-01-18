package com.project.backend.Repository;

import com.project.backend.Entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    @Query(value = "SELECT * FROM Subscriber WHERE Subscriber.status = 'ACTIVE'", nativeQuery = true)
    List<Subscriber> getActiveSubscribers();

}
