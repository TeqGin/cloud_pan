package com.example.cloud.domain;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Type {
    @Id
    private String typeName;
}
