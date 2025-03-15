package com.github.konarjg.SWIFTCodeAPI.response;

import java.util.List;

public class SwiftCodesByCountryResponse {
    private String countryISO2;
    private String countryName;
    private List<CountrySwiftCodeResponse> swiftCodes;

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

    public List<CountrySwiftCodeResponse> getSwiftCodes() {
        return swiftCodes;
    }

    public void setSwiftCodes(List<CountrySwiftCodeResponse> swiftCodes) {
        this.swiftCodes = swiftCodes;
    }
}
