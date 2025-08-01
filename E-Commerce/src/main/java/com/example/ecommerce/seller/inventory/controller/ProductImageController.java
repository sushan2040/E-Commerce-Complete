package com.example.ecommerce.seller.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductImageController {

	// Define the base path where the images are stored
    private static final String IMAGE_BASE_PATH = "/home/sweet-kevin/IdeaProjects/E-Commerce/ecommerce/ecommerce/PRODUCT_IMAGES/";

    @GetMapping("/products/images/{productId}/{imageName}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String productId, @PathVariable String imageName) throws IOException {
        // Construct the image file path
        String imagePath = IMAGE_BASE_PATH + productId + "/" + imageName;
        File imageFile = new File(imagePath);

        // Check if the image exists
        if (!imageFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Read the image file into a byte array
        try (InputStream in = new FileInputStream(imageFile)) {
            byte[] imageBytes = in.readAllBytes();

            // Determine the MIME type of the image
            String mimeType = "image/" + imageName.substring(imageName.lastIndexOf('.') + 1);

            // Return the image as a response entity with the appropriate content type and status
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                    .body(imageBytes);
        }
    }

}
