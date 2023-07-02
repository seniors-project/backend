package com.seniors.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPageRequest {

    private Integer page;

    private Integer size;

    private String sort;
}