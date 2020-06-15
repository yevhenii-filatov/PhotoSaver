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

@Data
@Entity
@Table(indexes = {@Index(name = "IDX_STATE", columnList = "link")})
@EqualsAndHashCode(of = {"link"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkState {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(length = 500)
    String link;

    @Column
    Boolean inited = false;

    @Column
    Boolean mapped = false;

    @Column(name = "computer_name")
    String computerName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "take_at")
    LocalDateTime takeAt;

}
