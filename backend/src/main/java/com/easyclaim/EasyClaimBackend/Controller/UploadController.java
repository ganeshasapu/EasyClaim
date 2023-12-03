package com.easyclaim.EasyClaimBackend.Controller;

import com.easyclaim.EasyClaimBackend.UseCase.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easyclaim.EasyClaimBackend.Entity.LifeClaim;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload_life/current")
    public String uploadCurrentLife(@RequestBody LifeClaim claim) throws ExecutionException, InterruptedException {

        return uploadService.uploadLife("Current", claim);

    }

    @PostMapping("/upload_life/historical")
    public String uploadHistoricalLife(@RequestBody LifeClaim claim) throws ExecutionException, InterruptedException {

        return uploadService.uploadLife("Historical", claim);

    }

}
