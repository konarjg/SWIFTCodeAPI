package com.github.konarjg.SWIFTCodeAPI.service;

import com.github.konarjg.SWIFTCodeAPI.component.SwiftCodeParser;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SwiftCodeService {
    private final SwiftCodeParser swiftCodeParser;
    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeService(SwiftCodeParser swiftCodeParser, SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftCodeParser = swiftCodeParser;
    }

    public List<SwiftCode> findAllByCountryISO2(String countryISO2) {
        return swiftCodeRepository.findAllByCountryISO2(countryISO2);
    }

    public SwiftCode findBySwiftCode(String swiftCode) {
        return swiftCodeRepository.findBySwiftCode(swiftCode);
    }

    public boolean saveSwiftCode(SwiftCode swiftCode) {
        try {
            if (swiftCode.getSwiftCode().length() < 8) {
                return false;
            }

            String parentCode = swiftCode.getSwiftCode().substring(0, 8);
            SwiftCode parentHeadquarters = swiftCodeRepository.findParentHeadquarters(parentCode);

            if (parentHeadquarters != null) {
                if (parentHeadquarters.getBranches() == null) {
                    parentHeadquarters.setBranches(new ArrayList<>());
                }

                parentHeadquarters.getBranches().add(swiftCode);
                swiftCodeRepository.save(parentHeadquarters);
            }

            if (swiftCode.isHeadquarter()) {
                List<SwiftCode> branches = swiftCodeRepository.findBranches(parentCode);
                swiftCode.setBranches(branches);
            }

            swiftCodeRepository.save(swiftCode);
            return true;
        } catch (DataIntegrityViolationException | NullPointerException exception) {
            return false;
        }
    }

    @Transactional
    public boolean deleteBySwiftCode(String swiftCode) {
        try {
            if (swiftCodeRepository.findBySwiftCode(swiftCode) == null) {
                return false;
            }

            swiftCodeRepository.deleteBySwiftCode(swiftCode);
            return true;
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public void initializeDatabaseWithParsedCodesWhenEmpty(InputStream stream) {
        if (swiftCodeRepository.count() > 0) {
            return;
        }

        List<SwiftCode> codes = swiftCodeParser.parseCodes(stream);
        System.out.println("Loaded " + codes.size() + " codes from file");
        swiftCodeRepository.saveAll(codes);
    }
}
