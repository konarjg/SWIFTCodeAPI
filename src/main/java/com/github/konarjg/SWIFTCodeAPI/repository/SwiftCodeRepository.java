package com.github.konarjg.SWIFTCodeAPI.repository;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    public List<SwiftCode> findAllByCountryISO2(String countryISO2);
    public SwiftCode findBySwiftCode(String swiftCode);
    @Query("SELECT s FROM SwiftCodes s WHERE SUBSTRING(s.swiftCode, 1, 8) = :parentCode AND s.isHeadquarter = true")
    public SwiftCode findParentHeadquarters(@Param("parentCode") String parentCode);
    public void deleteBySwiftCode(String swiftCode);
}
