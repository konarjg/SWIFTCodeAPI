package com.github.konarjg.SWIFTCodeAPI.controller;

import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProvider;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCodeFactory;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;
import com.github.konarjg.SWIFTCodeAPI.response.*;
import com.github.konarjg.SWIFTCodeAPI.response.factory.DeleteSwiftCodeResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.response.factory.GetSwiftCodeResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.response.factory.GetSwiftCodesByCountryResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.response.factory.PostSwiftCodeResponseFactory;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class SwiftCodeController {
    private final SwiftCodeService swiftCodeService;

    public SwiftCodeController(SwiftCodeService swiftCodeService, SpreadsheetDataProvider spreadsheetDataProvider) {
        this.swiftCodeService = swiftCodeService;
        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(spreadsheetDataProvider.createOrLoadDataFile());
    }

    @GetMapping("/swift-codes/{swift-code}")
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable("swift-code") String swiftCode) {
        SwiftCode code = swiftCodeService.findBySwiftCode(swiftCode);

        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching SWIFT code was found in the database!");
        }

        if (!code.isHeadquarter()) {
            return ResponseEntity.status(HttpStatus.OK).body(GetSwiftCodeResponseFactory.createBranchCodeResponse(code));
        }

        return ResponseEntity.status(HttpStatus.OK).body(GetSwiftCodeResponseFactory.createHeadquartersCodeResponse(code));
    }

    @GetMapping("/swift-codes/country/{countryISO2code}")
    public ResponseEntity<?> getAllSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2) {
        List<SwiftCode> codes = swiftCodeService.findAllByCountryISO2(countryISO2);

        if (codes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching SWIFT codes for this country code were found in the database!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(GetSwiftCodesByCountryResponseFactory.createGetSwiftCodesByCountryResponse(codes));
    }

    @PostMapping("/swift-codes")
    public ResponseEntity<PostSwiftCodeResponse> addSwiftCode(@RequestBody PostSwiftCodeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        SwiftCode code = SwiftCodeFactory.createSwiftCode(request);

        boolean result = swiftCodeService.saveSwiftCode(code);

        if (!result) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(PostSwiftCodeResponseFactory
                            .createPostSwiftCodeResponse("SWIFT code could not be added because it either already exists or is invalid!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostSwiftCodeResponseFactory
                        .createPostSwiftCodeResponse("Successfully added SWIFT code to database!"));
    }

    @DeleteMapping("/swift-codes/{swift-code}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable("swift-code") String swiftCode) {
        boolean result = swiftCodeService.deleteBySwiftCode(swiftCode);

        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(DeleteSwiftCodeResponseFactory
                            .createDeleteSwiftCodeResponse("Could not delete provided SWIFT code because no matching SWIFT code was found in the database!"));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(DeleteSwiftCodeResponseFactory
                        .createDeleteSwiftCodeResponse("Provided SWIFT code was successfully removed from the database!"));
    }
}
