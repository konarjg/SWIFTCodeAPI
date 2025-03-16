package com.github.konarjg.SWIFTCodeAPI.response.factory;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.response.GetSwiftCodeByCountry;
import com.github.konarjg.SWIFTCodeAPI.response.GetSwiftCodesByCountryResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class GetSwiftCodesByCountryResponseFactory {
    private static GetSwiftCodeByCountry createGetSwiftCodeByCountryResponse(SwiftCode code) {
        GetSwiftCodeByCountry response = new GetSwiftCodeByCountry();
        response.setSwiftCode(code.getSwiftCode());
        response.setCountryISO2(code.getCountryISO2());
        response.setBankName(code.getBankName());
        response.setAddress(code.getAddress());
        response.setIsHeadquarter(code.isHeadquarter());

        return response;
    }

    public static GetSwiftCodesByCountryResponse createGetSwiftCodesByCountryResponse(List<SwiftCode> codes) {
        GetSwiftCodesByCountryResponse response = new GetSwiftCodesByCountryResponse();
        response.setCountryName(codes.getFirst().getCountryName());
        response.setCountryISO2(codes.getFirst().getCountryISO2());
        response.setSwiftCodes(codes.stream().map(GetSwiftCodesByCountryResponseFactory::createGetSwiftCodeByCountryResponse).toList());

        return response;
    }
}
