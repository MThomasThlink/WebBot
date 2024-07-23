package com.thlink.webBot.report.reply.list;

import lombok.Data;

@Data
public class ReportReplyStats {

    private Long smallest, highest;
    private Double averageS, averageMin, averageHour, stdDev;
    private double values[];
}
