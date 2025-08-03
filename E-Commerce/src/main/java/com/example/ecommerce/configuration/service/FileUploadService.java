package com.example.ecommerce.configuration.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.ecommerce.constants.Constants;
import com.example.ecommerce.seller.inventory.masters.ProductImages;
import com.example.ecommerce.seller.inventory.masters.ProductMaster;
import com.example.ecommerce.seller.inventory.service.ProductService;

import jakarta.annotation.PostConstruct;

@Service
public class FileUploadService {

    private final Environment environment;
    private final ProductService productService;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initialize() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

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
           // file.transferTo(photoFile);
            uploadFile(file,productId + fileName + ".webp");
            System.out.println("File uploaded successfully: " + photoFile.getAbsolutePath());

            // Save metadata
            ProductImages image = new ProductImages();
            ProductMaster master = new ProductMaster();
            master.setProductId(productId.intValue());
            image.setProductMaster(master);
            String savedPath="https://"+bucketName+".s3."+Regions.AP_SOUTHEAST_1+".amazonaws.com/"+productId + fileName + ".webp";
            image.setImagePath(savedPath);
            image.setDeleted(Constants.NOT_DELETED);
            image.setStatus(Constants.STATUS_ACTIVE);
            if (isPrimary) {
                image.setIsPrimary(Constants.IMAGE_IS_PRIMARY);
            }

            productService.saveProductImage(image);
            System.out.println("Saved image metadata for: " + fileName);

        } catch (IllegalStateException e) {
            throw new FileUploadException("Failed to save file " + fileName + ": " + e.getMessage(), e);
        }
    }
    public void uploadFile(MultipartFile multipartFile,String filename) throws FileUploadException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = dateTimeFormatter.format(LocalDate.now());
        String filePath = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());
            filePath = filename;
            PutObjectResult objectResult=s3Client.putObject(bucketName, filePath, multipartFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new FileUploadException("Error occurred in file upload ==> "+e.getMessage());
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