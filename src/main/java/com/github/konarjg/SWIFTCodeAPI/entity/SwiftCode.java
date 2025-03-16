package com.github.konarjg.SWIFTCodeAPI.entity;

import com.google.gson.Gson;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "SwiftCodes")
public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long swiftCodeId;

    @Column(name = "bankName", nullable = false)
    private String bankName;
    @Column(name = "countryISO2", nullable = false)
    private String countryISO2;
    @Column(name = "countryName", nullable = false)
    private String countryName;
    @Column(name = "address", nullable = true)
    private String address;
    @Column(name = "swiftCode", nullable = false, unique = true)
    private String swiftCode;
    @Column(name = "isHeadquarter", nullable = false)
    private boolean isHeadquarter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "headquartersCodeId")
    private List<SwiftCode> branches;

    public long getSwiftCodeId() {
        return swiftCodeId;
    }

    public void setSwiftCodeId(long swiftCodeId) {
        this.swiftCodeId = swiftCodeId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode.replaceAll("\\s", "");
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    public List<SwiftCode> getBranches() {
        return branches;
    }

    public void setBranches(List<SwiftCode> branches) {
        this.branches = branches;
    }
}
