package com.mts.auditapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class SaveFile {
    private static final Logger logger = LoggerFactory.getLogger(SaveFile.class);

    @Value("${bucket-name}")
    private String bucket_name;

    //http://www.java2s.com/example/java/aws/save-file-to-amazon-s3-using-the-aws-sdk-for-java.html
    public void writeToS3(String key, String contents) {
        try
        {
            AmazonS3 s3 = new AmazonS3Client();

            String bucketName =this.bucket_name;
            String bucketKey = key;

            PutObjectRequest request = new PutObjectRequest(bucketName, key, createSampleFile(bucketKey, contents));
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(request);

            logger.info("File uploaded successfully to S3 bucket");
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
            logger.error("Error Message: " + ace.getMessage());
        }
        catch (Exception exception)
        {
            logger.error("Error Message: " + exception.toString());
        }
    }

    /**
     * Creates a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     *
     * @throws IOException
     */
    private static File createSampleFile(String fileName, String fileContents) throws IOException {
        File file = File.createTempFile(fileName, ".json");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(fileContents);
        writer.close();

        return file;
    }
}
