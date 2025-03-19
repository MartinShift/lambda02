package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
    roleName = "hello_world-role",
    isPublishVersion = true,
    aliasName = "${lambdas_alias_name}",
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        
        Map<String, Object> requestContext = (Map<String, Object>) input.get("requestContext");
        Map<String, Object> http = (Map<String, Object>) requestContext.get("http");
        String path = (String) http.get("path");
        String httpMethod = (String) http.get("method");

        if ("/hello".equals(path) && "GET".equals(httpMethod)) {
            resultMap.put("statusCode", 200);
            bodyMap.put("statusCode", 200);
            bodyMap.put("message", "Hello from Lambda");
        } else {
            resultMap.put("statusCode", 400);
            bodyMap.put("statusCode", 400);
            bodyMap.put("message", String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s", path, httpMethod));
        }

        try {
            resultMap.put("body", objectMapper.writeValueAsString(bodyMap));
        } catch (Exception e) {
            resultMap.put("body", "Error converting body to JSON");
        }
        resultMap.put("headers", headers);
        resultMap.put("isBase64Encoded", false);

        return resultMap;
    }
}