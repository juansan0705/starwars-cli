package org.mb.tech.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

public interface JsonService {
    JsonNode readJsonFile(File file) throws IOException;
}