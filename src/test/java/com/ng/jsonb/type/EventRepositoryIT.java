package com.ng.jsonb.type;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.jsonb.model.AdditionalData;
import com.ng.jsonb.model.EventData;
import com.ng.jsonb.model.EventLogging;
import com.ng.jsonb.rest.EventRepository;
import com.ng.jsonb.support.AbstractDbConfig;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@EnableTransactionManagement
public class EventRepositoryIT extends AbstractDbConfig {

  ObjectMapper jsonConverter = new ObjectMapper();

  @Autowired
  private EventRepository repository;

  @Test
  public void shouldSaveAndRetrieve() throws Exception {

    EventData eventData = new EventData("eventDataStr1", "eventDataStr2");
    AdditionalData additionalData = new AdditionalData("127.0.0.1", "8.8.8.8", true);

    EventLogging event = new EventLogging();
    event.setCreatedBy("John Doe");
    event.setEventData(eventData);
    event.setAdditionalData(additionalData);

    EventLogging resultSave = repository.save(event);

    Optional<EventLogging> resultEntity = repository.findById(event.getEventId());

    Assert.assertNotNull(resultSave);
    Assert.assertTrue(resultEntity.isPresent());
    String resultSaveJson = jsonConverter.writeValueAsString(resultSave);
    String resultEntityJson = jsonConverter.writeValueAsString(resultEntity.get());
    System.out.println(resultEntityJson);
    Assert.assertEquals(resultEntityJson, resultSaveJson);
  }
}
