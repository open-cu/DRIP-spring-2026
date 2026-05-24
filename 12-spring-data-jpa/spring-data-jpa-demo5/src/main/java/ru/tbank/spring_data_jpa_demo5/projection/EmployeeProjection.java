package ru.tbank.spring_data_jpa_demo5.projection;

import java.math.BigDecimal;

public record EmployeeProjection(
        String name,
        BigDecimal salary
) {}
