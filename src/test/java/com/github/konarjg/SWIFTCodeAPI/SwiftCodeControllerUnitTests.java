package com.github.konarjg.SWIFTCodeAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProvider;
import com.github.konarjg.SWIFTCodeAPI.controller.SwiftCodeController;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequestFactory;
import com.github.konarjg.SWIFTCodeAPI.response.DeleteSwiftCodeResponse;
import com.github.konarjg.SWIFTCodeAPI.response.PostSwiftCodeResponse;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SwiftCodeController.class)
@Import(TestSecurityConfig.class)
public class SwiftCodeControllerUnitTests {
    @MockitoBean
    private SwiftCodeService swiftCodeService;
    @MockitoBean
    private SpreadsheetDataProvider spreadsheetDataProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getSwiftCodeDetails_whenSwiftCodeIsNotInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String swiftCode = "test";
        mockMvc.perform(get("/v1/swift-codes/" + swiftCode)).andExpect(status().isNotFound());
    }

    @Test
    public void getSwiftCodeDetails_whenHeadquartersSwiftCodeIsInDatabase_shouldReturnValidResponse() throws Exception {
        String swiftCode = "BCHICLRMXXX";

        SwiftCode expected = new SwiftCode();
        expected.setSwiftCode(swiftCode);
        expected.setCountryName("CHILE");
        expected.setCountryISO2("CL");
        expected.setBankName("BANCO DE CHILE");
        expected.setHeadquarter(true);

        when(swiftCodeService.findBySwiftCode(swiftCode)).thenReturn(expected);

        mockMvc.perform(get("/v1/swift-codes/" + swiftCode)).andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value(swiftCode))
                .andExpect(jsonPath("$.countryISO2").value(expected.getCountryISO2()))
                .andExpect(jsonPath("$.countryName").value(expected.getCountryName()))
                .andExpect(jsonPath("$.isHeadquarter").value(expected.isHeadquarter()))
                .andExpect(jsonPath("$.bankName").value(expected.getBankName()))
                .andExpect(jsonPath("$.address").value(expected.getAddress()));
    }

    @Test
    public void getSwiftCodeDetails_whenBranchSwiftCodeIsInDatabase_shouldReturnValidResponse() throws Exception {
        String swiftCode = "BCHICLRMIOB";
        SwiftCode expected = new SwiftCode();
        expected.setSwiftCode(swiftCode);
        expected.setCountryName("CHILE");
        expected.setCountryISO2("CL");
        expected.setBankName("BANCO DE CHILE");
        expected.setHeadquarter(false);

        when(swiftCodeService.findBySwiftCode(swiftCode)).thenReturn(expected);

        mockMvc.perform(get("/v1/swift-codes/" + swiftCode)).andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value(swiftCode))
                .andExpect(jsonPath("$.countryISO2").value(expected.getCountryISO2()))
                .andExpect(jsonPath("$.countryName").value(expected.getCountryName()))
                .andExpect(jsonPath("$.isHeadquarter").value(expected.isHeadquarter()))
                .andExpect(jsonPath("$.bankName").value(expected.getBankName()))
                .andExpect(jsonPath("$.address").value(expected.getAddress()));
    }

    @Test
    public void getAllSwiftCodesByCountry_whenNoMatchingCodesInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String countryISO2 = "US";

        when(swiftCodeService.findAllByCountryISO2(countryISO2)).thenReturn(List.of());

        mockMvc.perform(get("/v1/swift-codes/country/" + countryISO2)).andExpect(status().isNotFound());
    }

    @Test
    public void getAllSwiftCodesByCountry_whenOneMatchingCodeInDatabase_shouldReturnValidResponse() throws Exception {
        String countryISO2 = "US";
        SwiftCode expected = new SwiftCode();
        expected.setSwiftCode("BCHICLRMIOB");
        expected.setCountryName("CHILE");
        expected.setCountryISO2(countryISO2);
        expected.setBankName("BANCO DE CHILE");
        expected.setHeadquarter(false);

        List<SwiftCode> codes = new ArrayList<>();
        codes.add(expected);

        when(swiftCodeService.findAllByCountryISO2(countryISO2)).thenReturn(codes);

        mockMvc.perform(get("/v1/swift-codes/country/" + countryISO2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value(expected.getCountryISO2()))
                .andExpect(jsonPath("$.countryName").value(expected.getCountryName()))
                .andExpect(jsonPath("$.swiftCodes[0].address").value(expected.getAddress()))
                .andExpect(jsonPath("$.swiftCodes[0].bankName").value(expected.getBankName()))
                .andExpect(jsonPath("$.swiftCodes[0].countryISO2").value(expected.getCountryISO2()))
                .andExpect(jsonPath("$.swiftCodes[0].isHeadquarter").value(expected.isHeadquarter()))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value(expected.getSwiftCode()));
    }

    @Test
    public void addSwiftCode_whenCodeIsAlreadyInDatabase_shouldReturnUnprocessableEntityStatus() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("BCHICLRMIOB");
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(false);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void addSwiftCode_whenCodeIsAlreadyInDatabase_shouldReturnValidResponseMessage() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("BCHICLRMIOB");
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(false);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.message")
                        .value("SWIFT code could not be added because it either already exists or is invalid!"));
    }


    @Test
    public void addSwiftCode_whenCodeIsInvalid_shouldReturnUnprocessableEntityStatus() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(false);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void addSwiftCode_whenCodeIsInvalid_shouldReturnValidResponseMessage() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(false);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.message")
                        .value("SWIFT code could not be added because it either already exists or is invalid!"));
    }

    @Test
    public void addSwiftCode_whenCodeDoesNotExist_shouldReturnCreatedStatus() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("BCHICLRMIOB");
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(true);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    public void addSwiftCode_whenCodeDoesNotExist_shouldReturnValidResponseMessage() throws Exception {
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("BCHICLRMIOB");
        code.setCountryName("CHILE");
        code.setCountryISO2("CL");
        code.setBankName("BANCO DE CHILE");
        code.setHeadquarter(false);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(code);
        String body = new ObjectMapper().writeValueAsString(request);

        when(swiftCodeService.saveSwiftCode(any())).thenReturn(true);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.message").value("Successfully added SWIFT code to database!"));
    }

    @Test
    public void deleteSwiftCode_whenSwiftCodeIsNotInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String swiftCode = "test";

        when(swiftCodeService.deleteBySwiftCode(swiftCode)).thenReturn(false);

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteSwiftCode_whenSwiftCodeIsNotInDatabase_shouldReturnValidResponseMessage() throws Exception {
        String swiftCode = "test";

        when(swiftCodeService.deleteBySwiftCode(swiftCode)).thenReturn(false);

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(jsonPath("$.message")
                        .value("Could not delete provided SWIFT code because no matching SWIFT code was found in the database!"));
    }

    @Test
    public void deleteSwiftCode_whenSwiftCodeIsInDatabase_shouldReturnOkStatus() throws Exception {
        String swiftCode = "AAISALTRXXX";

        when(swiftCodeService.deleteBySwiftCode(swiftCode)).thenReturn(true);

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSwiftCode_whenSwiftCodeIsInDatabase_shouldReturnValidResponseMessage() throws Exception {
        String swiftCode = "AAISALTRXXX";

        when(swiftCodeService.deleteBySwiftCode(swiftCode)).thenReturn(true);

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(jsonPath("$.message")
                        .value("Provided SWIFT code was successfully removed from the database!"));
    }
}
