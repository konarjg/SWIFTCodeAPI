package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParserComponent;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SwiftCodeServiceIntegrationTests {
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    private File createTempSpreadsheet(int id) {
        String path = "test" + id + ".xlsx";

        try(InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            File file = Files.createTempFile("test" + id, ".xlsx").toFile();
            file.deleteOnExit();

            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    public void findAllByCountryISO2_whenDatabaseIsEmpty_shouldReturnEmptyList() {
        //Arrange
        String countryISO2 = "US";
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(countryISO2);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllByCountryISO2_whenNoValidCodesArePresent_shouldReturnEmptyList() {
        //Arrange
        String countryISO2 = "US";
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        swiftCodeRepository.save(code);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(countryISO2);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllByCountryISO2_whenValidCodeIsPresent_shouldReturnListContainingTheCode() {
        //Arrange
        String countryISO2 = "US";
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        SwiftCode code1 = new SwiftCode();
        code1.setCountryISO2("US");
        code1.setBankName("bank");
        code1.setHeadquarter(true);
        code1.setSwiftCode("BADADADAXXX");
        code1.setCountryName("United States");

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(countryISO2);

        //Assert
        assertTrue(result.stream().anyMatch(c -> c.getSwiftCode().equals(code1.getSwiftCode())));
    }

    @Test
    public void findAllByCountryISO2_whenValidCodeIsPresent_shouldNotContainCodeWithInvalidCountryISO2() {
        //Arrange
        String countryISO2 = "US";
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        SwiftCode code1 = new SwiftCode();
        code1.setCountryISO2("US");
        code1.setBankName("bank");
        code1.setHeadquarter(true);
        code1.setSwiftCode("BADADADAXXX");
        code1.setCountryName("United States");

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(countryISO2);

        //Assert
        assertFalse(result.stream().anyMatch(c -> c.getSwiftCode().equals(code.getSwiftCode())));
    }

    @Test
    public void findBySwiftCode_whenProvidedCodeIsNull_shouldReturnNull() {
        //Arrange
        String swiftCode = null;
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        SwiftCode code1 = new SwiftCode();
        code1.setCountryISO2("US");
        code1.setBankName("bank");
        code1.setHeadquarter(true);
        code1.setSwiftCode("BADADADAXXX");
        code1.setCountryName("United States");

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(swiftCode);

        //Assert
        assertNull(result);
    }

    @Test
    public void findBySwiftCode_whenProvidedCodeIsNotInDatabase_shouldReturnNull() {
        //Arrange
        String swiftCode = "adadadawdada";
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        SwiftCode code1 = new SwiftCode();
        code1.setCountryISO2("US");
        code1.setBankName("bank");
        code1.setHeadquarter(true);
        code1.setSwiftCode("BADADADAXXX");
        code1.setCountryName("United States");

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(swiftCode);

        //Assert
        assertNull(result);
    }

    @Test
    public void findBySwiftCode_whenProvidedCodeIsValid_shouldReturnCorrectSwiftCode() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        SwiftCode code1 = new SwiftCode();
        code1.setCountryISO2("US");
        code1.setBankName("bank");
        code1.setHeadquarter(true);
        code1.setSwiftCode("BADADADAXXX");
        code1.setCountryName("United States");

        String swiftCode = code1.getSwiftCode();

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(swiftCode);

        //Assert
        assertEquals(swiftCode, result.getSwiftCode());
    }

    @Test
    public void saveSwiftCode_whenProvidedCodeIsNull_shouldReturnFalse() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode swiftCode = null;

        //Act
        boolean result = swiftCodeService.saveSwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void saveSwiftCode_whenProvidedCodeIsInvalid_shouldReturnFalse() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode swiftCode = new SwiftCode();

        //Act
        boolean result = swiftCodeService.saveSwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void saveSwiftCode_whenProvidedCodeIsValid_shouldReturnTrue() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        //Act
        boolean result = swiftCodeService.saveSwiftCode(code);

        //Assert
        assertTrue(result);
    }

    @Test
    public void saveSwiftCode_whenProvidedCodeIsValid_shouldAddSwiftCodeToDatabase() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        //Act
        swiftCodeService.saveSwiftCode(code);

        //Assert
        assertNotNull(swiftCodeRepository.findBySwiftCode(code.getSwiftCode()));
    }

    @Test
    public void deleteBySwiftCode_whenProvidedCodeIsNull_shouldReturnFalse() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        String swiftCode = null;
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        swiftCodeRepository.save(code);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void deleteBySwiftCode_whenProvidedCodeIsInvalid_shouldReturnFalse() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        String swiftCode = "XDADADSD";
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        swiftCodeRepository.save(code);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void deleteBySwiftCode_whenProvidedCodeIsValid_shouldReturnTrue() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        swiftCodeRepository.save(code);

        //Act
        boolean result = swiftCodeService.saveSwiftCode(code);

        //Assert
        assertTrue(result);
    }

    @Test
    public void deleteBySwiftCode_whenProvidedCodeIsValid_shouldDeleteSwiftCodeToDatabase() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        swiftCodeRepository.save(code);

        //Act
        swiftCodeService.deleteBySwiftCode(code.getSwiftCode());

        //Assert
        assertNull(swiftCodeRepository.findBySwiftCode(code.getSwiftCode()));
    }

    @Test
    public void initializeDatabaseWithParsedCodesWhenEmpty_whenDatabaseIsNotEmpty_shouldNotParseCodes() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        SwiftCode code = new SwiftCode();
        code.setCountryISO2("UK");
        code.setBankName("bank");
        code.setHeadquarter(true);
        code.setSwiftCode("ADADADAADXXX");
        code.setCountryName("United Kingdom");

        File file = createTempSpreadsheet(4);
        swiftCodeRepository.save(code);

        //Act
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(file);

        //Assert
        assertEquals(1, swiftCodeRepository.count());
    }

    @Test
    public void initializeDatabaseWithParsedCodesWhenEmpty_whenDatabaseIsEmpty_shouldParseCodesAndFillDatabase() {
        //Arrange
        SwiftCodeParser swiftCodeParser = new SwiftCodeParserComponent();
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);
        File file = createTempSpreadsheet(4);

        //Act
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(file);

        //Assert
        assertEquals(1061, swiftCodeRepository.count());
    }
}
