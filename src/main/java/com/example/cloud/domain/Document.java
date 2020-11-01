package com.example.cloud.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Document {
    @Id
    private String id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "uper_id")
    private String uperId;

    @Column(name = "date")
    private String date;

    @Column(name = "size")
    private String size;

}
