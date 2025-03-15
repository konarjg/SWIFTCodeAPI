package com.github.konarjg.SWIFTCodeAPI.controller;

import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProvider;
import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProviderComponent;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;
import com.github.konarjg.SWIFTCodeAPI.response.*;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@RestController
@RequestMapping("/v1")
public class SwiftCodeController {
    private final SwiftCodeService swiftCodeService;
    private final SpreadsheetDataProvider spreadsheetDataProvider;

    public SwiftCodeController(SwiftCodeService swiftCodeService, SpreadsheetDataProvider spreadsheetDataProvider) {
        this.swiftCodeService = swiftCodeService;
        this.spreadsheetDataProvider = spreadsheetDataProvider;
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(spreadsheetDataProvider.createOrLoadDataFile());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/swift-codes/{swift-code}")
    public GetSwiftCodeResponse getSwiftCodeDetails(@PathVariable("swift-code") String swiftCode) {
        SwiftCode code = swiftCodeService.findBySwiftCode(swiftCode);

        if (code == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!code.isHeadquarter()) {
            BranchCodeResponse branchResponse = new BranchCodeResponse();
            branchResponse.setAddress(code.getAddress());
            branchResponse.setBankName(code.getBankName());
            branchResponse.setCountryISO2(code.getCountryISO2());
            branchResponse.setCountryName(code.getCountryName());
            branchResponse.setIsHeadquarter(code.isHeadquarter());
            branchResponse.setSwiftCode(code.getSwiftCode());

            return branchResponse;
        }

        HeadquartersCodeResponse headquartersResponse = new HeadquartersCodeResponse();
        headquartersResponse.setAddress(code.getAddress());
        headquartersResponse.setBankName(code.getBankName());
        headquartersResponse.setCountryISO2(code.getCountryISO2());
        headquartersResponse.setCountryName(code.getCountryName());
        headquartersResponse.setIsHeadquarter(code.isHeadquarter());
        headquartersResponse.setSwiftCode(code.getSwiftCode());

        if (code.getBranches() == null) {
            code.setBranches(new ArrayList<>());
        }

        headquartersResponse.setBranches(code.getBranches().stream().map(c -> {
            HeadquartersBranchCodeResponse branchResponse = new HeadquartersBranchCodeResponse();
            branchResponse.setAddress(c.getAddress());
            branchResponse.setBankName(c.getBankName());
            branchResponse.setCountryISO2(c.getCountryISO2());
            branchResponse.setSwiftCode(c.getSwiftCode());
            branchResponse.setHeadquarter(c.isHeadquarter());

            return branchResponse;
        }).toList());

        return headquartersResponse;
    }

    @GetMapping("/swift-codes/country/{countryISO2code}")
    public SwiftCodesByCountryResponse getAllSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2) {
        return null;
    }

    @PostMapping("/swift-codes")
    public PostSwiftCodeResponse addSwiftCode(@RequestBody PostSwiftCodeRequest request) {
        return null;
    }

    @DeleteMapping("/swift-codes/{swift-code}")
    public DeleteSwiftCodeResponse deleteSwiftCode(@PathVariable("swift-code") String swiftCode) {
        return null;
    }
}
