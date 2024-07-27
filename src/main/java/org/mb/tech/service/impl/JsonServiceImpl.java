package org.mb.tech.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mb.tech.service.JsonService;

import java.io.File;
import java.io.IOException;

public class JsonServiceImpl implements JsonService{

    private final ObjectMapper objectMapper;

    public JsonServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public JsonNode readJsonFile(File file) throws IOException {
        return objectMapper.readTree(file);
    }
}