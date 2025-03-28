package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParserComponent;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwiftCodeParserComponentTests {
    private InputStream createTempSpreadsheet(int id) {
        String path = "test" + id + ".xlsx";

        try(InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            File file = Files.createTempFile("test" + id, ".xlsx").toFile();
            file.deleteOnExit();

            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return new FileInputStream(file);
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    public void parseCode_whenCodeEndsWithXXX_shouldParseHeadquartersCode() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        HashMap<String, String> data = new HashMap<>();
        data.put("countryISO2", "AL");
        data.put("swiftCode", "AAISALTRXXX");
        data.put("codeType", "BIC11");
        data.put("name", "UNITED BANK OF ALBANIA SH.A");
        data.put("address", "HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        data.put("townName", "TIRANA");
        data.put("countryName", "ALBANIA");
        data.put("timeZone", "Europe/Tirane");

        //Act
        SwiftCode code = parser.parseCode(data);

        //Assert
        assertTrue(code.isHeadquarter());
    }

    @Test
    public void parseCode_whenCodeDoesNotEndWithXXX_shouldParseBranchCode() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        HashMap<String, String> data = new HashMap<>();
        data.put("countryISO2", "CL");
        data.put("swiftCode", "BCHICLR10R7");
        data.put("address", "");
        data.put("countryName", "CHILE");

        //Act
        SwiftCode code = parser.parseCode(data);

        //Assert
        assertFalse(code.isHeadquarter());
    }

    @Test
    public void parseCodes_whenFileDoesIsEmpty_shouldReturnEmptyList() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = Files.createTempFile("test", ".xlsx").toFile();
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(new FileInputStream(file));

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileContainsValidCodes_shouldReturnNonEmptyList() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(0);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertFalse(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileContainsHeadquarterCode_shouldReturnListWithHeadquarterCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(0);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter()));
    }

    @Test
    public void parseCodes_whenFileContainsBranchCode_shouldReturnListWithBranchCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(1);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertTrue(result.stream().anyMatch(code -> !code.isHeadquarter()));
    }

    @Test
    public void parseCodes_whenFileContainsBranchCodeWithEightCharacterMatchingPreviousHeadquarter_shouldReturnListWithHeadquarterCodeThatHasBranchCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(2);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter() && !code.getBranches().isEmpty()));
    }

    @Test
    public void parseCodes_whenFileContainsHeadquarterCodeWithEightCharacterMatchingPreviousHeadquarter_shouldReturnListWithHeadquarterCodeThatHasBranchHeadquarterCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(3);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter() && !code.getBranches().isEmpty()));
    }

    @Test
    public void parseCodes_forFinalProductionFile_shouldReturnListWithAllCodes() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(4);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertEquals(1061, result.size());
    }

    @Test
    public void parseCodes_forFinalProductionFile_shouldContainExactly696HeadquartersCodes() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(4);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertEquals(696, result.stream().filter(SwiftCode::isHeadquarter).count());
    }

    @Test
    public void parseCodes_forFinalProductionFile_shouldContainExactly365BranchCodes() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        InputStream stream = createTempSpreadsheet(4);

        //Act
        List<SwiftCode> result = parser.parseCodes(stream);

        //Assert
        assertEquals(365, result.stream().filter(code -> !code.isHeadquarter()).count());
    }
}
