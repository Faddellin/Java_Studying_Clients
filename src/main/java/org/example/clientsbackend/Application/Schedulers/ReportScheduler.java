package org.example.clientsbackend.Application.Schedulers;

import lombok.extern.slf4j.Slf4j;
import org.example.clientsbackend.Application.ServicesInterfaces.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ReportScheduler {

    private final ReportService reportService;

    public ReportScheduler(ReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "#{@schedulerConfig.getCron()}")
    public void createReport() {
        try{
            reportService.createReport();
        }catch (IOException e){
            System.out.println("Error creating report: " + e.getMessage());
        }

    }

}
