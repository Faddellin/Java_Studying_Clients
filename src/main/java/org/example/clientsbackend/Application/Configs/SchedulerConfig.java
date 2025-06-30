package org.example.clientsbackend.Application.Configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Data
public class SchedulerConfig {
    @Value(value = "${scheduler.enabled}")
    private Boolean enabled;

    @Value(value = "${scheduler.report-folder}")
    private String pathToReportFolder;

    @Value(value = "${scheduler.cron}")
    private String cron;

    public String getCron(){
        return enabled ? cron : "-";
    }
}
