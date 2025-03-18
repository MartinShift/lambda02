package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
    roleName = "hello_world-role",
    isPublishVersion = true,
    aliasName = "${lambdas_alias_name}",
    logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> resultMap = new HashMap<>();
        
        String path = (String) ((Map<String, Object>) input.get("requestContext")).get("resourcePath");
        String httpMethod = (String) ((Map<String, Object>) input.get("requestContext")).get("httpMethod");

        if ("/hello".equals(path) && "GET".equals(httpMethod)) {
            resultMap.put("statusCode", 200);
            resultMap.put("body", "{\"message\":\"Hello from Lambda\"}");
        } else {
            resultMap.put("statusCode", 400);
            resultMap.put("body", String.format("{\"error\":\"Bad request: %s %s\"}", httpMethod, path));
        }

        return resultMap;
    }
}