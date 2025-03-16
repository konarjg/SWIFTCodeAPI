package com.github.konarjg.SWIFTCodeAPI.controller;

import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProvider;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;
import com.github.konarjg.SWIFTCodeAPI.response.*;
import com.github.konarjg.SWIFTCodeAPI.response.factory.GetSwiftCodeResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.response.factory.GetSwiftCodesByCountryResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            return GetSwiftCodeResponseFactory.createBranchCodeResponse(code);
        }

        return GetSwiftCodeResponseFactory.createHeadquartersCodeResponse(code);
    }

    @GetMapping("/swift-codes/country/{countryISO2code}")
    public GetSwiftCodesByCountryResponse getAllSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2) {
        List<SwiftCode> codes = swiftCodeService.findAllByCountryISO2(countryISO2);

        if (codes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return GetSwiftCodesByCountryResponseFactory.createGetSwiftCodesByCountryResponse(codes);
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
