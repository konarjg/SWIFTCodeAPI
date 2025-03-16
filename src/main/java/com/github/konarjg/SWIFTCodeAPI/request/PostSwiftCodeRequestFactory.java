package com.github.konarjg.SWIFTCodeAPI.request;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;

public class PostSwiftCodeRequestFactory {
    public static PostSwiftCodeRequest createSwiftCodeRequest(SwiftCode code) {
        PostSwiftCodeRequest request = new PostSwiftCodeRequest();
        request.setAddress(code.getAddress());
        request.setCountryISO2(code.getCountryISO2());
        request.setCountryName(code.getCountryName());
        request.setBankName(code.getBankName());
        request.setSwiftCode(code.getSwiftCode());
        request.setIsHeadquarter(code.isHeadquarter());
        return request;
    }
}
