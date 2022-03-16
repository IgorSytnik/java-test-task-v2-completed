package com.cryptocurrency.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price {
    private Long tmsp;
    private String price;
}
