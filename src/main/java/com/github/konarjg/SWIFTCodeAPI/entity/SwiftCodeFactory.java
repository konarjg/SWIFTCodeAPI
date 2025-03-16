package com.github.konarjg.SWIFTCodeAPI.entity;

import com.github.konarjg.SWIFTCodeAPI.request.PostSwiftCodeRequest;

public final class SwiftCodeFactory {
    public static SwiftCode createSwiftCode(PostSwiftCodeRequest request) {
        SwiftCode code = new SwiftCode();
        code.setAddress(request.getAddress());
        code.setBankName(request.getBankName());
        code.setCountryISO2(request.getCountryISO2());
        code.setCountryName(request.getCountryName());
        code.setSwiftCode(request.getSwiftCode());
        code.setHeadquarter(request.getIsHeadquarter());

        return code;
    }
}
