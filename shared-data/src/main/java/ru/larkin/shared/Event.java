package ru.larkin.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}