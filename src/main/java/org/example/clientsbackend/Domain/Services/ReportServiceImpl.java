package org.example.clientsbackend.Domain.Services;


import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ReportService;
import org.example.clientsbackend.Application.Utilities.ExcelHelper;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReportServiceImpl implements ReportService {

    private final ClientRepository _clientRepository;

    public ReportServiceImpl(ClientRepository clientRepository) {
        _clientRepository = clientRepository;
    }

    @Value("${scheduler.report-folder}")
    private String pathToReportFolder;

    public byte[] getReport() throws ExceptionWrapper {
        Path pathToFile = Paths.get(pathToReportFolder + "/" + "report.xlsx").toAbsolutePath();
        File file = new File(pathToFile.toString());

        if (!file.exists()) {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("Report", "Report doesn't exist");
            throw  entityNotFoundException;
        }

        try(FileInputStream fileInputStream = new FileInputStream(file)){
            return IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createReport() throws IOException {
        File directory = new File(pathToReportFolder);
        if(!directory.exists()) directory.mkdirs();

        Path pathToFile = Paths.get(pathToReportFolder + "/" + "report.xlsx").toAbsolutePath();
        File file = new File(pathToFile.toString());

        Workbook xml = new XSSFWorkbook();

        List<List<String>> clientsTable = new ArrayList<>(List.of(
                    List.of("ID клиента", "Имя", "Email", "Возраст", "Город", "Улица", "Менеджер")
            ));
        List<Client> clients = _clientRepository.findAll();

        for(Client client : clients){
            boolean clientHasAddress = client.getAddress() != null;
            boolean clientHasManager = client.getManager() != null;
            clientsTable.add(List.of(
                    client.getId().toString(),
                    client.getUsername(),
                    client.getEmail(),
                    client.getAge().toString(),
                    clientHasAddress ? client.getAddress().getCity() : "",
                    clientHasAddress ? client.getAddress().getStreet() : "",
                    clientHasManager ? client.getManager().getUsername() : "")
            );
        }

        Sheet tableSheet = ExcelHelper.createTableSheet(xml, clientsTable, "Report");

        for(int i = 0; i < clientsTable.get(0).size(); i++){
            tableSheet.autoSizeColumn(i);
            tableSheet.setColumnWidth(i, (int)(tableSheet.getColumnWidth(i) * 1.1));
        }

        xml.write(new FileOutputStream(file));
        xml.close();
    }

}
