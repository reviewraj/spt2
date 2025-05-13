package com.tekpyramid.sp.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class TicketTemplate {
    @Id
    private String id;

    private String templateName;

    private List<CustomField> customFields;
}
