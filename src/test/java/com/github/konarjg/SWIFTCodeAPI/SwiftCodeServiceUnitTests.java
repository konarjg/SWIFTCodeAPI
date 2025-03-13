package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwiftCodeServiceUnitTests {
    @Test
    public void findAllByCountryISO2_shouldReturnListContainingOnlySwiftCodesWithGivenCountryISO2() {
        //Arrange
        SwiftCode code1 = new SwiftCode();
        code1.setSwiftCode("HQ123456");
        code1.setBankName("Bank A");
        code1.setCountryISO2("US");
        code1.setCountryName("United States");
        code1.setHeadquarter(false);

        SwiftCode code2 = new SwiftCode();
        code2.setSwiftCode("BR123457");
        code2.setBankName("Bank B");
        code2.setCountryISO2("US");
        code2.setCountryName("United States");
        code2.setHeadquarter(false);

        List<SwiftCode> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findAllByCountryISO2(code1.getCountryISO2())).thenReturn(codes);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeRepository);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(code1.getCountryISO2());

        //Assert
        assertTrue(result.stream().allMatch(c -> c.getCountryISO2().equals(code1.getCountryISO2())));
    }

    @Test
    public void findAllByCountryISO2_shouldNotContainCodesWithDifferentCountryISO2() {
        //Arrange
        SwiftCode code1 = new SwiftCode();
        code1.setSwiftCode("HQ123456");
        code1.setBankName("Bank A");
        code1.setCountryISO2("US");
        code1.setCountryName("United States");
        code1.setHeadquarter(false);

        SwiftCode code2 = new SwiftCode();
        code2.setSwiftCode("BR123457");
        code2.setBankName("Bank B");
        code2.setCountryISO2("US");
        code2.setCountryName("United States");
        code2.setHeadquarter(false);

        List<SwiftCode> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findAllByCountryISO2(code1.getCountryISO2())).thenReturn(codes);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeRepository);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(code1.getCountryISO2());

        //Assert
        assertFalse(result.stream().anyMatch(c -> !c.getCountryISO2().equals(code1.getCountryISO2())));
    }

    @Test
    public void findAllByCountryISO2_whenDatabaseDoesNotContainValidSwiftCodes_shouldReturnEmptyList() {
        //Arrange
        String countryISO2 = "US";
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findAllByCountryISO2(countryISO2)).thenReturn(List.of());

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeRepository);

        //Act
        List<SwiftCode> result = swiftCodeService.findAllByCountryISO2(countryISO2);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findBySwiftCode_shouldReturnCodeWithGivenSwiftCode() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(false);

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findBySwiftCode(code.getSwiftCode())).thenReturn(code);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeRepository);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(code.getSwiftCode());

        //Assert
        assertEquals(result.getSwiftCode(), code.getSwiftCode());
    }

    @Test
    public void findBySwiftCode_whenDatabaseDoesNotContainValidCodes_shouldReturnNull() {
        //Arrange
        String swiftCode = "HQ123456";
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findBySwiftCode(swiftCode)).thenReturn(null);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeRepository);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(swiftCode);

        //Assert
        assertNull(result);
    }
}
