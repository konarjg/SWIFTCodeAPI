package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

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

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);
        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

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

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

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

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

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

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        SwiftCode result = swiftCodeService.findBySwiftCode(swiftCode);

        //Assert
        assertNull(result);
    }

    @Test
    public void saveSwiftCode_whenDataIsNull_shouldReturnFalse() {
        //Arrange
        SwiftCode data = null;
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.save(data)).thenThrow(new NullPointerException());

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.saveSwiftCode(data);

        //Assert
        assertFalse(result);
    }

    @Test
    public void saveSwiftCode_whenDataIsInvalid_shouldReturnFalse() {
        //Arrange
        SwiftCode data = new SwiftCode();
        data.setBankName("test");
        data.setCountryISO2("test");
        data.setHeadquarter(false);
        data.setCountryName("test");
        data.setAddress("");

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.save(data)).thenThrow(new DataIntegrityViolationException("Data integrity violated"));

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.saveSwiftCode(data);

        //Assert
        assertFalse(result);
    }

    @Test
    public void saveSwiftCode_whenDataIsValid_shouldReturnTrue() {
        //Arrange
        SwiftCode data = new SwiftCode();
        data.setSwiftCode("HQ123456");
        data.setBankName("Bank A");
        data.setCountryISO2("US");
        data.setCountryName("United States");
        data.setHeadquarter(false);

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.saveSwiftCode(data);

        //Assert
        assertTrue(result);
    }

    @Test
    public void saveSwiftCode_whenDatabaseContainsHeadquartersForSavedCode_shouldAddTheCodeAsBranchOfHeadquarters() {
        //Arrange
        SwiftCode headquarter = new SwiftCode();
        headquarter.setSwiftCode("HQ123456XXX");
        headquarter.setBankName("Bank A");
        headquarter.setCountryISO2("US");
        headquarter.setCountryName("United States");
        headquarter.setHeadquarter(true);
        headquarter.setBranches(new ArrayList<>());

        SwiftCode data = new SwiftCode();
        data.setSwiftCode("HQ123456");
        data.setBankName("Bank B");
        data.setCountryISO2("US");
        data.setCountryName("United States");
        data.setHeadquarter(false);

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findParentHeadquarters(data.getSwiftCode().substring(0, 8))).thenReturn(headquarter);
        when(swiftCodeRepository.save(headquarter)).thenReturn(headquarter);
        when(swiftCodeRepository.save(data)).thenReturn(data);

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        swiftCodeService.saveSwiftCode(data);

        //Assert
        assertTrue(headquarter.getBranches().contains(data));
    }

    @Test
    public void deleteBySwiftCode_whenCodeIsNull_shouldReturnFalse() {
        //Arrange
        String swiftCode = null;
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        doThrow(new NullPointerException()).when(swiftCodeRepository).deleteBySwiftCode(swiftCode);

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void deleteBySwiftCode_whenCodeIsEmpty_shouldReturnFalse() {
        //Arrange
        String swiftCode = "";
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findBySwiftCode(swiftCode)).thenReturn(null);
        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void deleteBySwiftCode_whenCodeDoesNotExistInTheDatabase_shouldReturnFalse() {
        //Arrange
        String swiftCode = "HX131313XXX";
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findBySwiftCode(swiftCode)).thenReturn(null);

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertFalse(result);
    }

    @Test
    public void deleteBySwiftCode_whenCodeIsValid_shouldReturnTrue() {
        //Arrange
        String swiftCode = "HX131313XXX";

        SwiftCode data = new SwiftCode();
        data.setSwiftCode(swiftCode);
        data.setCountryName("United States");
        data.setHeadquarter(true);
        data.setBankName("Test");
        data.setCountryISO2("US");

        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.findBySwiftCode(swiftCode)).thenReturn(data);

        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        //Assert
        assertTrue(result);
    }

    @Test
    public void initializeDatabaseWithParsedCodesWhenEmpty_whenDatabaseIsNotEmpty_shouldNotAddEntities() {
        //Arrange
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.count()).thenReturn(1L);
        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);
        File file = mock(File.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(file);

        //Assert
        verify(swiftCodeParser, times(0)).parseCodes(file);
    }

    @Test
    public void initializeDatabaseWithParsedCodesWhenEmpty_whenDatabaseIsEmpty_shouldAddEntities() {
        //Arrange
        SwiftCodeRepository swiftCodeRepository = mock(SwiftCodeRepository.class);
        when(swiftCodeRepository.count()).thenReturn(0L);
        SwiftCodeParser swiftCodeParser = mock(SwiftCodeParser.class);
        File file = mock(File.class);

        SwiftCodeService swiftCodeService = new SwiftCodeService(swiftCodeParser, swiftCodeRepository);

        //Act
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(file);

        //Assert
        verify(swiftCodeParser, times(1)).parseCodes(file);
    }
}
