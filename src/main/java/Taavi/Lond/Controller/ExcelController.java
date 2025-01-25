package Taavi.Lond.Controller;

import Taavi.Lond.DataRow;
import Taavi.Lond.Service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    @Autowired
    private ExcelService excelService;


    @GetMapping("/data")
    public List<DataRow> getExcelData() throws IOException {
        return excelService.getExcelData();
    }
}