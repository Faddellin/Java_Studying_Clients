package org.example.clientsbackend.Presentation.Controllers;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.ServicesInterfaces.ReportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportsController {

    private final ReportService _reportService;

    public ReportsController(ReportService reportService) {
        _reportService = reportService;
    }

    @GetMapping(path = "reports/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] GetReport() throws ExceptionWrapper {
        return _reportService.getReport();
    }

}
