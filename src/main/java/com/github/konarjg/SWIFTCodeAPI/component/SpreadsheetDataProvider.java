package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;

import java.io.File;

public interface SpreadsheetDataProvider {
    public File createOrLoadDataFile();
}
