package com.prayer.api.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Prayer {
    private String name;
    private String time;
}
