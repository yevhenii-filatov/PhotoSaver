package com.introlab.photosaver.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Yevhenii Filatov
 * @since 6/19/20
 **/

@Data
@Entity
@Table(name = "link")
@EqualsAndHashCode(of = {"value"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Link {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(name = "value")
    String value;

    @Column(name = "profile")
    String pageSource;

    @Column(name = "activities")
    String activitiesSource;

    @Column(name = "processed")
    Boolean parsed;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "computer_name")
    String computerName;

    @Column(name = "deployed")
    Boolean deployed;
}
