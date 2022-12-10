package com.proximity.proximity.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerRequest {
    @JsonProperty("customer_name")
    public String customerName;
    public double lat;
    public double lon;
}
