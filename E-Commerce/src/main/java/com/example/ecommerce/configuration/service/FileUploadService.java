package com.example.ecommerce.configuration.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.seller.inventory.service.ProductService;

@Service
public class FileUploadService {

    private final Environment environment;
    private final ProductService productService;

    public FileUploadService(Environment environment, ProductService productService) {
        this.environment = environment;
        this.productService = productService;
    }

    public void saveProductImages(Long productId, List<MultipartFile> files, MultipartFile primaryImage) throws FileUploadException {
        if (productId == null || productId <= 0) {
            throw new FileUploadException("Invalid product ID");
        }

        String basePath = environment.getProperty("product.images.path");
        if (basePath == null || basePath.isEmpty()) {
            throw new FileUploadException("Base path not configured");
        }

        // Construct directory path with year/month
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        String photoUploadPath = basePath + File.separator + year + File.separator + month;

        // Create directory if it doesn't exist
        File photoDir = new File(photoUploadPath);
        if (!photoDir.exists()) {
            if (!photoDir.mkdirs()) {
                throw new FileUploadException("Failed to create directory: " + photoUploadPath);
            }
            System.out.println("Created directory: " + photoUploadPath);
        }

        // Check if directory is writable
        if (!photoDir.canWrite()) {
            throw new FileUploadException("Directory is not writable: " + photoUploadPath);
        }

        // Save multiple files
        if (files != null && !files.isEmpty()) {
            int i = 1;
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    saveFile(file, productId, photoUploadPath, "productPhoto_" + i, false);
                    i++;
                } else {
                    System.out.println("Skipping empty or null file at index " + i);
                }
            }
        } else {
            System.out.println("No files provided for upload");
        }

        // Save primary image
        if (primaryImage != null && !primaryImage.isEmpty()) {
            saveFile(primaryImage, productId, photoUploadPath, "productPhoto_p", true);
        } else {
            System.out.println("No primary image provided");
        }
    }

    private void saveFile(MultipartFile file, Long productId, String photoUploadPath, String fileName, boolean isPrimary)
            throws FileUploadException {
        try {
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new FileUploadException("Invalid file type for " + fileName + ": " + contentType);
            }

            // Construct file path
            File photoFile = new File(photoUploadPath, productId + fileName + ".webp");

            // Check if file exists
            if (photoFile.exists()) {
                System.out.println("Overwriting existing file: " + photoFile.getAbsolutePath());
                photoFile.delete();
            }

            // Save the file
            file.transferTo(photoFile);
            System.out.println("File uploaded successfully: " + photoFile.getAbsolutePath());

            // Save metadata
            ProductImages image = new ProductImages();
            ProductMaster master = new ProductMaster();
            master.setProductId(productId.intValue());
            image.setProductMaster(master);
            image.setImagePath(environment.getProperty("product.virtual.images.path") + File.separator
                    + productId + fileName + ".webp");
            image.setDeleted(Constants.NOT_DELETED);
            image.setStatus(Constants.STATUS_ACTIVE);
            if (isPrimary) {
                image.setIsPrimary(Constants.IMAGE_IS_PRIMARY);
            }

            productService.saveProductImage(image);
            System.out.println("Saved image metadata for: " + fileName);

        } catch (IllegalStateException e) {
            throw new FileUploadException("Failed to save file " + fileName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new FileUploadException("Failed to save file " + fileName + ": " + e.getMessage(), e);
        }
    }
}

// Custom exception
class FileUploadException extends Exception {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}