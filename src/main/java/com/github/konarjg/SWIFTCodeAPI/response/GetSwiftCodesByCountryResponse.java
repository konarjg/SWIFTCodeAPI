package com.github.konarjg.SWIFTCodeAPI.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "countryISO2",
        "countryName",
        "swiftCodes"
})
public class GetSwiftCodesByCountryResponse {
    private String countryISO2;
    private String countryName;
    private List<GetSwiftCodeByCountry> swiftCodes;

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<GetSwiftCodeByCountry> getSwiftCodes() {
        return swiftCodes;
    }

    public void setSwiftCodes(List<GetSwiftCodeByCountry> swiftCodes) {
        this.swiftCodes = swiftCodes;
    }
}
