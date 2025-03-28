package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.Executable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SwiftCodeRepositoryTests {
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @Test
    public void findAllByCountryISO2_shouldReturnListOfSwiftCodesWithGivenCountryISO2() {
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

        SwiftCode code3 = new SwiftCode();
        code3.setSwiftCode("BR123458");
        code3.setBankName("Bank C");
        code3.setCountryISO2("UK");
        code3.setCountryName("United Kingdom");
        code3.setHeadquarter(false);

        swiftCodeRepository.save(code1);
        swiftCodeRepository.save(code2);
        swiftCodeRepository.save(code3);

        //Act
        List<SwiftCode> result = swiftCodeRepository.findAllByCountryISO2("US");

        //Assert
        assertTrue(result.stream().allMatch(code -> code.getCountryISO2().equals("US")));
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

        SwiftCode code3 = new SwiftCode();
        code3.setSwiftCode("BR123458");
        code3.setBankName("Bank C");
        code3.setCountryISO2("UK");
        code3.setCountryName("United Kingdom");
        code3.setHeadquarter(false);

        swiftCodeRepository.save(code1);
        swiftCodeRepository.save(code2);
        swiftCodeRepository.save(code3);

        //Act
        List<SwiftCode> result = swiftCodeRepository.findAllByCountryISO2("US");

        //Assert
        assertFalse(result.stream().anyMatch(code -> !code.getCountryISO2().equals("US")));
    }

    @Test
    public void findAllByCountryISO2_shouldContainAllGivenCodesWithGivenCountryISO2() {
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

        SwiftCode code3 = new SwiftCode();
        code3.setSwiftCode("BR123458");
        code3.setBankName("Bank C");
        code3.setCountryISO2("UK");
        code3.setCountryName("United Kingdom");
        code3.setHeadquarter(false);

        swiftCodeRepository.save(code1);
        swiftCodeRepository.save(code2);
        swiftCodeRepository.save(code3);

        //Act
        List<SwiftCode> result = swiftCodeRepository.findAllByCountryISO2("US");

        //Assert
        assertEquals(2, result.size());
    }

    @Test
    public void findAllByCountryISO2_whenDatabaseDoesNotContainValidCodes_shouldReturnEmptyList() {
        //Arrange
        String countryISO2 = "US";

        //Act
        List<SwiftCode> result = swiftCodeRepository.findAllByCountryISO2(countryISO2);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findBySwiftCode_shouldReturnCodeWithGivenSwiftCode() {
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

        swiftCodeRepository.save(code1);
        swiftCodeRepository.save(code2);

        //Act
        SwiftCode result = swiftCodeRepository.findBySwiftCode(code1.getSwiftCode());

        //Assert
        assertEquals(code1.getSwiftCode(), result.getSwiftCode());
    }

    @Test
    public void findBySwiftCode_whenDatabaseDoesNotContainValidCodes_shouldReturnNull() {
        //Arrange
        String swiftCode = "HQ123456";

        //Act
        SwiftCode result = swiftCodeRepository.findBySwiftCode(swiftCode);

        //Assert
        assertNull(result);
    }

    @Test
    public void deleteBySwiftCode_shouldDeleteCodeWithGivenSwiftCode() {
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

        swiftCodeRepository.save(code1);
        swiftCodeRepository.save(code2);

        //Act
        swiftCodeRepository.deleteBySwiftCode(code1.getSwiftCode());

        //Assert
        List<SwiftCode> result = swiftCodeRepository.findAll();
        assertFalse(result.stream().anyMatch(code -> code.getSwiftCode().equals(code1.getSwiftCode())));
    }

    @Test
    public void findParentHeadquarters_whenDatabaseDoesNotContainValidCodes_shouldReturnNull() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456XXX");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(true);

        String parentCode = "KX513412";

        swiftCodeRepository.save(code);

        //Act
        SwiftCode result = swiftCodeRepository.findParentHeadquarters(parentCode);

        //Assert
        assertNull(result);
    }

    @Test
    public void findParentHeadquarters_whenDatabaseContainsValidCode_shouldReturnParentHeadquarters() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456XXX");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(true);

        String parentCode = "HQ123456";

        swiftCodeRepository.save(code);

        //Act
        SwiftCode result = swiftCodeRepository.findParentHeadquarters(parentCode);

        //Assert
        assertEquals(code.getSwiftCode(), result.getSwiftCode());
    }

    @Test
    public void findBranches_whenDatabaseDoesNotContainValidCodes_shouldReturnEmptyList() {
        //Arrange
        String parentCode = "KX513412";

        //Act
        List<SwiftCode> branches = swiftCodeRepository.findBranches(parentCode);

        //Assert
        assertTrue(branches.isEmpty());
    }

    @Test
    public void findBranches_whenDatabaseContainsValidCodes_shouldReturnAllBranches() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456XXX");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(true);

        SwiftCode code1 = new SwiftCode();
        code1.setSwiftCode("HQ123456ADA");
        code1.setBankName("Bank A");
        code1.setCountryISO2("US");
        code1.setCountryName("United States");
        code1.setHeadquarter(false);

        String parentCode = "HQ123456";

        swiftCodeRepository.save(code);
        swiftCodeRepository.save(code1);

        //Act
        List<SwiftCode> result = swiftCodeRepository.findBranches(parentCode);

        //Assert
        assertEquals(2, result.size());
    }

    @Test
    public void save_whenSwiftCodeAlreadyExists_shouldThrowDataIntegrityException() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(false);
        swiftCodeRepository.save(code);

        SwiftCode code1 = new SwiftCode();
        code1.setSwiftCode("HQ123456");
        code1.setBankName("Bank B");
        code1.setCountryISO2("US");
        code1.setCountryName("United States");
        code1.setHeadquarter(false);

        //Act
        Runnable action = () -> swiftCodeRepository.save(code1);

        //Assert
        assertThrows(DataIntegrityViolationException.class, action::run);
    }

    @Test
    public void save_whenSwiftCodeWithoutWhitespacesAlreadyExists_shouldThrowDataIntegrityException() {
        //Arrange
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("HQ123456");
        code.setBankName("Bank A");
        code.setCountryISO2("US");
        code.setCountryName("United States");
        code.setHeadquarter(false);
        swiftCodeRepository.save(code);

        SwiftCode code1 = new SwiftCode();
        code1.setSwiftCode("  HQ 123 456   ");
        code1.setBankName("Bank B");
        code1.setCountryISO2("US");
        code1.setCountryName("United States");
        code1.setHeadquarter(false);

        //Act
        Runnable action = () -> swiftCodeRepository.save(code1);

        //Assert
        assertThrows(DataIntegrityViolationException.class, action::run);
    }
}
