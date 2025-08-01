package com.example.ecommerce.configuration.config;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luciad.imageio.webp.WebPWriteParam;

public class WebPCompressor {

	public static final Logger logger = LoggerFactory.getLogger(WebPCompressor.class);

	public static void compressWebP(File inputFile, File outputFile, float quality, String outputPath, String fileName)
	        throws IOException {
	    // Read the image from the input file
	    BufferedImage image = ImageIO.read(inputFile);

	    // Resize the image (if needed)
	    BufferedImage resizedImage = resizeImage(image, 800, 800);  // Resize to 800x800 or any desired size

	    // Obtain a WebP ImageWriter instance
	    ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

	    // Configure encoding parameters
	    WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
	    writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
	    writeParam.setCompressionQuality(quality); // Set the quality (0.0 to 1.0)

	    // Create a temporary output file
	    File tempOutputFile = new File(outputPath, fileName);
	    writer.setOutput(new FileImageOutputStream(tempOutputFile));

	    // Write the image to WebP format with compression
	    writer.write(null, new IIOImage(resizedImage, null, null), writeParam);

	    // Replace the original file with the compressed WebP file
	    tempOutputFile.renameTo(outputFile);
	    System.out.println("Compression and resize successful.");
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
	    // Create a resized version of the image
	    Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = resizedImage.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    return resizedImage;
	}

	public static String generateFixedRandomString(String patientId, int length) {
		try {
			// Create SHA-256 digest
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(patientId.getBytes());
			// Convert hash bytes to hexadecimal string
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0'); // Ensure two-digit hex
				}
				hexString.append(hex);
			}
			// Return the substring with the desired length
			return hexString.substring(0, Math.min(length, hexString.length()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error generating hash", e);
		}
	}
}