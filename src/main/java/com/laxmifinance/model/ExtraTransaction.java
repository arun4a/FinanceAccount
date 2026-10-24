package com.laxmifinance.model;

import lombok.Data;

@Data
public class ExtraTransaction {
    private String extraId;
    private String extraDate;
    private String extraPurpose;
    private String extraAmount;
}
