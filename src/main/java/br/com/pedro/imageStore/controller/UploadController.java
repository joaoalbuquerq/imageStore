package br.com.pedro.imageStore.controller;

import br.com.pedro.imageStore.service.UploadService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/images")
public class UploadController {

    private final UploadService uploadService;
    private final MinioClient minioClient;

    public UploadController(UploadService uploadService, MinioClient minioClient){
        this.uploadService = uploadService;
        this.minioClient = minioClient;
    }

    @PostMapping
    public void uploadImage(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        uploadService.uploadImage(file);
    }

    @GetMapping(value = "/{objectId}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable("objectId") String objectId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        var stream = uploadService.getImage(objectId);

        return IOUtils.toByteArray(stream);
    }

}
