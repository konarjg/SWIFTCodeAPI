package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface SwiftCodeParser {
    public SwiftCode parseCode(HashMap<String, String> data);
    public List<SwiftCode> parseCodes(File file);
}
