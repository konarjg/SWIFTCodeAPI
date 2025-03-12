package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Component
public class SwiftCodeParserComponent implements SwiftCodeParser {

    @Override
    public SwiftCode parseCode(HashMap<String, String> data) {
        return null;
    }

    @Override
    public List<SwiftCode> parseCodes(File file) {
        return List.of();
    }
}
