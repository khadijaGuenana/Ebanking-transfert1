package com.java.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MoneyValue{
    private double moneyValue;
    private Long idClient;
}