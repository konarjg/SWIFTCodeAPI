package com.github.konarjg.SWIFTCodeAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.repository.SwiftCodeRepository;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;
import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequestFactory;
import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class SwiftCodeControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SwiftCodeService swiftCodeService;
    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @AfterEach
    public void cleanUp() throws IOException {
        swiftCodeRepository.deleteAll();
        File file = new File(System.getProperty("user.dir") + "/src/test/resources/data.xlsx");

        swiftCodeService.initializeDatabaseWithParsedCodesWhenEmpty(new FileInputStream(file));
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER"})
    public void getSwiftCodeDetails_whenCodeIsNotInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String swiftCode = "test";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER"})
    public void getSwiftCodeDetails_whenCodeIsInDatabase_shouldReturnValidResponse() throws Exception {
        String swiftCode = "AAISALTRXXX";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address")
                        .value("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023"))
                .andExpect(jsonPath("$.bankName").value("UNITED BANK OF ALBANIA SH.A"))
                .andExpect(jsonPath("$.countryISO2").value("AL"))
                .andExpect(jsonPath("$.countryName").value("ALBANIA"))
                .andExpect(jsonPath("$.swiftCode").value(swiftCode))
                .andExpect(jsonPath("$.isHeadquarter").value(true));
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER"})
    public void getAllSwiftCodesByCountry_whenNoValidCodesExist_shouldReturnNotFoundStatus() throws Exception {
        String countryISO2 = "US";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/country/" + countryISO2))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER"})
    public void getAllSwiftCodesByCountry_whenValidCodesExist_shouldReturnOkStatus() throws Exception {
        String countryISO2 = "AL";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/country/" + countryISO2))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER"})
    public void getAllSwiftCodesByCountry_whenValidCodesExist_shouldReturnValidResponse() throws Exception {
        String countryISO2 = "AL";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/country/" + countryISO2))
                .andExpect(jsonPath("$.countryISO2").value(countryISO2))
                .andExpect(jsonPath("$.countryName").value("ALBANIA"));
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void addSwiftCode_whenCodeIsAlreadyInDatabase_shouldReturnUnprocessableEntityStatus() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("AAISALTRXXX");
        swiftCode.setCountryISO2("AL");
        swiftCode.setCountryName("ALBANIA");
        swiftCode.setAddress("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        swiftCode.setBankName("UNITED BANK OF ALBANIA");
        swiftCode.setHeadquarter(true);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(swiftCode);
        String body = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void addSwiftCode_whenCodeIsNotInDatabase_shouldReturnCreatedStatus() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("AAISALBTRXXX");
        swiftCode.setCountryISO2("AL");
        swiftCode.setCountryName("ALBANIA");
        swiftCode.setAddress("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        swiftCode.setBankName("UNITED BANK OF ALBANIA");
        swiftCode.setHeadquarter(true);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(swiftCode);
        String body = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void addSwiftCode_whenCodeIsNotInDatabase_shouldAddSwiftCodeToDatabase() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("AAISALBTRXXX");
        swiftCode.setCountryISO2("AL");
        swiftCode.setCountryName("ALBANIA");
        swiftCode.setAddress("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023");
        swiftCode.setBankName("UNITED BANK OF ALBANIA");
        swiftCode.setHeadquarter(true);

        PostSwiftCodeRequest request = PostSwiftCodeRequestFactory.createSwiftCodeRequest(swiftCode);
        String body = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON).content(body));

        assertNotNull(swiftCodeService.findBySwiftCode(swiftCode.getSwiftCode()));
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void deleteSwiftCode_whenCodeIsNotInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String swiftCode = "AAISALCTRXXX";

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void deleteSwiftCode_whenCodeIsInDatabase_shouldReturnOkStatus() throws Exception {
        String swiftCode = "AAISALTRXXX";

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="test", authorities = {"SCOPE_USER", "SCOPE_ADMIN"})
    public void deleteSwiftCode_whenCodeIsInDatabase_shouldDeleteTheCodeFromDatabase() throws Exception {
        String swiftCode = "AAISALTRXXX";

        mockMvc.perform(delete("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isOk());

        assertNull(swiftCodeService.findBySwiftCode(swiftCode));
    }
}
