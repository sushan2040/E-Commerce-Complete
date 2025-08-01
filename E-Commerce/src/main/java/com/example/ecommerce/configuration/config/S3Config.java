package com.example.ecommerce.configuration.config;

//@Configuration
public class S3Config {

//    @Value("${aws.access.key.id}")
//    private String accessKey;
//
//    @Value("${aws.secret.access.key}")
//    private String secretKey;
//
//    @Value("${aws.region}")
//    private String region;
//
//    @Value("${aws.s3.bucket.name}")
//    private String bucketName;
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.fromName(region))
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//    }
//    
//    @Bean
//    public String bucketName() {
//        return bucketName;
//    }
}