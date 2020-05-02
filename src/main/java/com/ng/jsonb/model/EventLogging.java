package com.ng.jsonb.model;

import com.ng.jsonb.types.JsonbType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_logging")
@TypeDefs({@TypeDef(name = JsonbType.JSONB_TYPE, typeClass = JsonbType.class)})
public class EventLogging {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id")
  private Integer eventId;

  @Type(
      type = JsonbType.JSONB_TYPE,
      parameters = {
          @Parameter(name = JsonbType.CLASS_TYPE, value = "com.ng.jsonb.model.EventData")
      })

  @Column(name = "event_data")
  private EventData eventData;

  @Type(
      type = JsonbType.JSONB_TYPE,
      parameters = {
          @Parameter(name = JsonbType.CLASS_TYPE, value = "com.ng.jsonb.model.AdditionalData")
      })

  @Column(name = "additional_data")
  private AdditionalData additionalData;


  @Column(name = "created_by")
  private String createdBy;

  @CreationTimestamp
  @Column(name = "created_on")
  private Date createdOn;
}
