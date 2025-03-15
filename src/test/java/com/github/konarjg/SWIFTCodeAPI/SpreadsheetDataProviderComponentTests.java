package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProvider;
import com.github.konarjg.SWIFTCodeAPI.component.SpreadsheetDataProviderComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SpreadsheetDataProviderComponentTests {
    @AfterEach
    public void cleanUp() throws IOException {
        File file = new File("data.xlsx");

        if (file.exists()) {
            Files.delete(file.toPath());
        }
    }

    @Test
    public void loadOrCreateDataFile_shouldNotThrowAnyExceptions() {
        //Arrange
        SpreadsheetDataProvider spreadsheetDataProvider = new SpreadsheetDataProviderComponent();

        //Act
        try {
            spreadsheetDataProvider.createOrLoadDataFile();
        }
        catch (Exception e) {
            //Assert
            fail();
        }
    }

    @Test
    public void loadOrCreateDataFile_whenFileIsNotInDirectory_shouldCreateDataFileFromResources() {
        //Arrange
        SpreadsheetDataProvider spreadsheetDataProvider = new SpreadsheetDataProviderComponent();

        //Act
        File file = spreadsheetDataProvider.createOrLoadDataFile();

        //Assert
        assertTrue(file.exists());
    }

    @Test
    public void loadOrCreateDataFile_whenFileIsInDirectory_shouldLoadFileFromDirectory() throws IOException {
        //Arrange
        SpreadsheetDataProvider spreadsheetDataProvider = new SpreadsheetDataProviderComponent();
        File file = Files.createFile(Path.of("data.xlsx")).toFile();

        //Act
        File result = spreadsheetDataProvider.createOrLoadDataFile();

        //Assert
        assertEquals(file.getAbsolutePath(), result.getAbsolutePath());
    }
}
