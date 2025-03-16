package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.service.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.util.List;

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

    @Test
    public void getSwiftCodeDetails_whenCodeIsNotInDatabase_shouldReturnNotFoundStatus() throws Exception {
        String swiftCode = "test";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/" + swiftCode))
                .andExpect(status().isNotFound());
    }

    @Test
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
}
