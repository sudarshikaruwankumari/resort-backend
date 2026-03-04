package com.example.ocean_view_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ImageUploadService {

    @Value("${imgbb.api.key}")
    private String apiKey;

    @Value("${imgbb.api.url}")
    private String apiUrl;

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // Create temp file
        File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(apiUrl + "?key=" + apiKey);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("image", new FileBody(tempFile));

            post.setEntity(builder.build());

            String response = client.execute(post, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);

            if (json.has("data") && json.get("data").has("url")) {
                return json.get("data").get("url").asText();
            } else {
                throw new IOException("ImgBB upload failed: " + json.toString());
            }
        } finally {
            tempFile.delete(); // clean up
        }
    }
}