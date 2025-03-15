package com.github.konarjg.SWIFTCodeAPI.component;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SpreadsheetDataProviderComponent implements SpreadsheetDataProvider {

    @Override
    public File createOrLoadDataFile() {
        String path = "data.xlsx";
        return new File(path);
    }
}
