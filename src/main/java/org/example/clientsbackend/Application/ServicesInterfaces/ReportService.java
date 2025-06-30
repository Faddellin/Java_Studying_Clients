package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import java.io.IOException;

public interface ReportService {
    byte[] getReport() throws ExceptionWrapper;
    void createReport() throws IOException;
}
