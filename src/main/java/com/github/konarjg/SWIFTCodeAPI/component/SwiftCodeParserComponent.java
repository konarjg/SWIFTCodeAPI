package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class SwiftCodeParserComponent implements SwiftCodeParser {

    @Override
    public SwiftCode parseCode(HashMap<String, String> data) {
        String countryISO2 = data.get("countryISO2");
        String swiftCode = data.get("swiftCode");
        String bankName = data.get("bankName");
        String address = data.get("address");
        String countryName = data.get("countryName");
        boolean isHeadquarter = swiftCode.endsWith("XXX");

        SwiftCode result = new SwiftCode();
        result.setCountryISO2(countryISO2);
        result.setSwiftCode(swiftCode);
        result.setBankName(bankName);
        result.setAddress(address);
        result.setCountryName(countryName);
        result.setHeadquarter(isHeadquarter);
        result.setHeadquartersCode(null);
        result.setBranches(new ArrayList<>());

        return result;
    }

    private List<SwiftCode> readSpreadsheet(Workbook workbook) {
        List<SwiftCode> result = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            if (row.getCell(0).getStringCellValue().isEmpty()) {
                break;
            }

            HashMap<String, String> data = new HashMap<>();

            data.put("countryISO2", row.getCell(0).getStringCellValue());
            data.put("swiftCode", row.getCell(1).getStringCellValue());
            data.put("bankName", row.getCell(3).getStringCellValue());
            data.put("address", row.getCell(4).getStringCellValue());
            data.put("countryName", row.getCell(7).getStringCellValue());

            SwiftCode code = parseCode(data);

            result.add(code);
        }

        return result;
    }

    @Override
    public List<SwiftCode> parseCodes(File file) {
        if (file == null || !file.exists()) {
            return List.of();
        }

        try (FileInputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            List<SwiftCode> result = readSpreadsheet(workbook);

            for (SwiftCode code : result) {
                SwiftCode parent = result.stream()
                        .filter(swiftCode -> swiftCode.isHeadquarter() && swiftCode.getSwiftCode()
                                .substring(0, 8)
                                .equals(code.getSwiftCode().substring(0, 8)))
                        .findFirst().orElse(null);

                if (parent != null) {
                    if (parent.getBranches() == null) {
                        parent.setBranches(new ArrayList<>());
                    }

                    parent.getBranches().add(code);
                }
            }

            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
