package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.File;
import java.io.IOException;

public class SchemaValidator {

    public static void validate(String jsonResponse, String schemaPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonResponse);
            JsonNode schemaNode = mapper.readTree(new File(schemaPath));

            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            ProcessingReport report = schema.validate(jsonNode);
            if (!report.isSuccess()) {
                throw new AssertionError("JSON schema validation failed: " + report);
            }
        } catch (IOException | ProcessingException e) {
            throw new RuntimeException("Error during schema validation", e);
        }
    }
}
