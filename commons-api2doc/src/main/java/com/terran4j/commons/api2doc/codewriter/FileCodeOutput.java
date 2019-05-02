package com.terran4j.commons.api2doc.codewriter;

import com.terran4j.commons.api2doc.other.utils.Files;

import java.io.File;

public class FileCodeOutput implements CodeOutput {

    private final String path;

    public FileCodeOutput(String path) {
        super();
        this.path = path;
    }

    @Override
    public void writeCodeFile(String fileName, String fileContent) {
        File file = new File(path + "/" + fileName);
        Files.writeFile(fileContent, file);
    }

    @Override
    public void setPercent(int percent) {
    }

    @Override
    public void log(String log, String... args) {
    }

}
