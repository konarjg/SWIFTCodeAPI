package com.github.konarjg.SWIFTCodeAPI.service;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwiftCodeService {
    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    public List<SwiftCode> findAllByCountryISO2(String countryISO2) {
        return swiftCodeRepository.findAllByCountryISO2(countryISO2);
    }

    public SwiftCode findBySwiftCode(String swiftCode) {
        return swiftCodeRepository.findBySwiftCode(swiftCode);
    }

    public void saveSwiftCode(SwiftCode swiftCode) {

    }

    public void deleteBySwiftCode(String swiftCode) {

    }
}
