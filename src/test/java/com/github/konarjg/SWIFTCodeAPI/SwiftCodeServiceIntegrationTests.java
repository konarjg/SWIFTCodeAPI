package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParserComponent;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SwiftCodeServiceIntegrationTests {
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

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
}
