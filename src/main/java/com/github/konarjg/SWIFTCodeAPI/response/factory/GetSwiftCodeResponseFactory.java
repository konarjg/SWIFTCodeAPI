package com.github.konarjg.SWIFTCodeAPI.response.factory;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import com.github.konarjg.SWIFTCodeAPI.response.BranchCodeResponse;
import com.github.konarjg.SWIFTCodeAPI.response.HeadquartersBranchCodeResponse;
import com.github.konarjg.SWIFTCodeAPI.response.HeadquartersCodeResponse;

import java.util.ArrayList;

public class GetSwiftCodeResponseFactory {

    private static HeadquartersBranchCodeResponse createHeadquartersBranchCodeResponse(SwiftCode code) {
        HeadquartersBranchCodeResponse branchResponse = new HeadquartersBranchCodeResponse();
        branchResponse.setAddress(code.getAddress());
        branchResponse.setBankName(code.getBankName());
        branchResponse.setCountryISO2(code.getCountryISO2());
        branchResponse.setSwiftCode(code.getSwiftCode());
        branchResponse.setHeadquarter(code.isHeadquarter());

        return branchResponse;
    }

    public static HeadquartersCodeResponse createHeadquartersCodeResponse(SwiftCode code) {
        HeadquartersCodeResponse headquartersResponse = new HeadquartersCodeResponse();
        headquartersResponse.setAddress(code.getAddress());
        headquartersResponse.setBankName(code.getBankName());
        headquartersResponse.setCountryISO2(code.getCountryISO2());
        headquartersResponse.setCountryName(code.getCountryName());
        headquartersResponse.setIsHeadquarter(code.isHeadquarter());
        headquartersResponse.setSwiftCode(code.getSwiftCode());

        if (code.getBranches() == null) {
            code.setBranches(new ArrayList<>());
        }

        headquartersResponse.setBranches(code.getBranches()
                .stream()
                .map(GetSwiftCodeResponseFactory::createHeadquartersBranchCodeResponse)
                .toList());

        return headquartersResponse;
    }

    public static BranchCodeResponse createBranchCodeResponse(SwiftCode code) {
        BranchCodeResponse branchResponse = new BranchCodeResponse();
        branchResponse.setAddress(code.getAddress());
        branchResponse.setBankName(code.getBankName());
        branchResponse.setCountryISO2(code.getCountryISO2());
        branchResponse.setCountryName(code.getCountryName());
        branchResponse.setIsHeadquarter(code.isHeadquarter());
        branchResponse.setSwiftCode(code.getSwiftCode());

        return branchResponse;
    }
}
