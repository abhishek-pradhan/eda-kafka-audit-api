package com.mts.auditapi.controller;

import com.mts.auditapi.service.SaveFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuditController {
    private static final Logger logger = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private SaveFile saveFile;

    // for simplicity's sake, this method is get, else would be post :)
    // this method serves purpose of manual testing of publishing message
    @GetMapping("/pub")
    public String publish() throws IOException {
        String key = "test_"+ UUID.randomUUID();
        String contents = "We have triggered this test from /api/pub";

        this.saveFile.writeToS3("city_"+ key, contents);

        return "S3 file uploaded with filename:" + key;
    }
}
