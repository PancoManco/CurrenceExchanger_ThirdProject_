package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void sendJsonResponse(HttpServletResponse resp, int status, Object data) throws IOException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        objectMapper.writeValue(resp.getWriter(), data);
    }
}