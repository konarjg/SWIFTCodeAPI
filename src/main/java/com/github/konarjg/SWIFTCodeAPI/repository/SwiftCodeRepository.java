package com.github.konarjg.SWIFTCodeAPI.repository;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    public List<SwiftCode> findAllByCountryISO2(String countryISO2);
    public SwiftCode findBySwiftCode(String swiftCode);
    public void deleteBySwiftCode(String swiftCode);
}
