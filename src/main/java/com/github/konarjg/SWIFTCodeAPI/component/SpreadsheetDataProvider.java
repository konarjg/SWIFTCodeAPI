package com.github.konarjg.SWIFTCodeAPI.component;

import com.github.konarjg.SWIFTCodeAPI.entity.SwiftCode;

import java.io.File;
import java.io.InputStream;

public interface SpreadsheetDataProvider {
    public InputStream createOrLoadDataFile();
}
