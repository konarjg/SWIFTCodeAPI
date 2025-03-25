package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.SwiftCodeApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Stream;

@Component
public class SpreadsheetDataProviderComponent implements SpreadsheetDataProvider {
    @Value("classpath:data.xlsx")
    private Resource dataFile;

    @Override
    public InputStream createOrLoadDataFile() {
        try {
            return dataFile.getInputStream();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
