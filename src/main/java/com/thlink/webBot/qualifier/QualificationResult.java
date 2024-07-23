package com.thlink.webBot.qualifier;

import lombok.Data;

@Data
public class QualificationResult {
    private Client client;
    private Double scoreM1, scoreM2, scoreM3;

    public QualificationResult() {
        this.scoreM1 = 0D;
        this.scoreM2 = 0D;
        this.scoreM2 = 0D;
    }
}
