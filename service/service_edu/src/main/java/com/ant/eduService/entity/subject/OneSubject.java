package com.ant.eduService.entity.subject;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class OneSubject {
    private String id;

    private String title;

    List<TwoSubject> children;
}
