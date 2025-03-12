package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParserComponent;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwiftCodeParserComponentTests {
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
    public void parseCodes_whenFileIsNull_shouldReturnEmptyList() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = null;

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileDoesNotExist_shouldReturnEmptyList() {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileDoesIsEmpty_shouldReturnEmptyList() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = Files.createTempFile("test", ".xlsx").toFile();
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileContainsValidCodes_shouldReturnNonEmptyList() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = Files.createTempFile("test", ".xlsx").toFile();
        Files.writeString(file.toPath(), "AL\tAAISALTRXXX\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane");
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertFalse(result.isEmpty());
    }

    @Test
    public void parseCodes_whenFileContainsHeadquarterCode_shouldReturnListWithHeadquarterCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = Files.createTempFile("test", ".xlsx").toFile();
        Files.writeString(file.toPath(), "AL\tAAISALTRXXX\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane");
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter()));
    }

    @Test
    public void parseCodes_whenFileContainsBranchCode_shouldReturnListWithBranchCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        File file = Files.createTempFile("test", ".xlsx").toFile();
        Files.writeString(file.toPath(), "AL\tAAISALTR\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane");
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.stream().anyMatch(code -> !code.isHeadquarter()));
    }

    @Test
    public void parseCodes_whenFileContainsBranchCodeWithEightCharacterMatchingPreviousHeadquarter_shouldReturnListWithHeadquarterCodeThatHasBranchCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        StringBuilder builder = new StringBuilder();
        builder.append("AL\tAAISALTRXXX\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane\n");
        builder.append("AL\tAAISALTR\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane\n");

        File file = Files.createTempFile("test", ".xlsx").toFile();
        Files.writeString(file.toPath(), builder.toString());
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter() && !code.getBranches().isEmpty()));
    }

    @Test
    public void parseCodes_whenFileContainsHeadquarterCodeWithEightCharacterMatchingPreviousHeadquarter_shouldReturnListWithHeadquarterCodeThatHasBranchHeadquarterCode() throws IOException {
        //Arrange
        SwiftCodeParser parser = new SwiftCodeParserComponent();
        StringBuilder builder = new StringBuilder();
        builder.append("AL\tAAISALTRXXX\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane\n");
        builder.append("AL\tAAISALTRBXXX\tBIC11\tUNITED BANK OF ALBANIA SH.A\tHYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023\tTIRANA\tALBANIA\tEurope/Tirane\n");

        File file = Files.createTempFile("test", ".xlsx").toFile();
        Files.writeString(file.toPath(), builder.toString());
        file.deleteOnExit();

        //Act
        List<SwiftCode> result = parser.parseCodes(file);

        //Assert
        assertTrue(result.stream().anyMatch(code -> code.isHeadquarter() && !code.getBranches().isEmpty()));
    }
}
