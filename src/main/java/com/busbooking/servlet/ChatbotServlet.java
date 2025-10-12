package com.busbooking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/api/chatbot")
public class ChatbotServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ChatbotServlet.class.getName());
    private static final String GEMINI_API_KEY = "AIzaSyAGxuAb_6y4x3b3naWkMKH7mkv_l64aE0A"; // Thay bằng API key của bạn
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
    
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Đọc message từ request
            String userMessage = request.getParameter("message");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(out, "Message không được để trống");
                return;
            }
            
            // Tạo prompt context cho bus booking
            String contextPrompt = "Bạn là trợ lý AI của hệ thống đặt vé xe buýt BusBooking. " +
                    "Hãy trả lời các câu hỏi về dịch vụ đặt vé xe, giá vé, tuyến đường, " +
                    "cách đặt vé, thanh toán và các thông tin liên quan đến việc đi lại bằng xe buýt. " +
                    "Trả lời bằng tiếng Việt và ngắn gọn, thân thiện. " +
                    "Câu hỏi: " + userMessage;
            
            // Gọi Gemini API
            String geminiResponse = callGeminiAPI(contextPrompt);
            
            // Trả về response
            JsonObject responseObj = new JsonObject();
            responseObj.addProperty("success", true);
            responseObj.addProperty("message", geminiResponse);
            
            out.write(gson.toJson(responseObj));
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error calling Gemini API", e);
            sendErrorResponse(out, "Có lỗi xảy ra khi xử lý câu hỏi: " + e.getMessage());
        } finally {
            out.close();
        }
    }
    
    private String callGeminiAPI(String prompt) throws Exception {
        // Tạo request body cho Gemini API
        JsonObject requestBody = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);
        
        // Thêm safety settings
        JsonArray safetySettings = new JsonArray();
        JsonObject safetySetting = new JsonObject();
        safetySetting.addProperty("category", "HARM_CATEGORY_HARASSMENT");
        safetySetting.addProperty("threshold", "BLOCK_MEDIUM_AND_ABOVE");
        safetySettings.add(safetySetting);
        requestBody.add("safetySettings", safetySettings);
        
        // Tạo HTTP request
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_API_URL + "?key=" + GEMINI_API_KEY))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();
        
        // Gửi request
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString());
        
        if (httpResponse.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: HTTP " + httpResponse.statusCode() + 
                    " - " + httpResponse.body());
        }
        
        // Parse response
        JsonObject responseJson = gson.fromJson(httpResponse.body(), JsonObject.class);
        
        if (responseJson.has("candidates") && responseJson.getAsJsonArray("candidates").size() > 0) {
            JsonObject candidate = responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject();
            if (candidate.has("content")) {
                JsonObject contentObj = candidate.getAsJsonObject("content");
                if (contentObj.has("parts") && contentObj.getAsJsonArray("parts").size() > 0) {
                    JsonObject partObj = contentObj.getAsJsonArray("parts").get(0).getAsJsonObject();
                    if (partObj.has("text")) {
                        return partObj.get("text").getAsString();
                    }
                }
            }
        }
        
        throw new RuntimeException("Không nhận được phản hồi hợp lệ từ Gemini API");
    }
    
    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        JsonObject errorObj = new JsonObject();
        errorObj.addProperty("success", false);
        errorObj.addProperty("error", errorMessage);
        out.write(gson.toJson(errorObj));
    }
}