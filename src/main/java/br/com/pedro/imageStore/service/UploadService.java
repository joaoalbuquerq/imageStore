package br.com.pedro.imageStore.service;

import br.com.pedro.imageStore.repository.UploadRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UploadService {

    private final UploadRepository uploadRepository;
    private final MinioClient  minioClient;

    public UploadService(UploadRepository uploadRepository, MinioClient minioClient) {
        this.uploadRepository = uploadRepository;
        this.minioClient = minioClient;
    }

    public void uploadImage(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        var objectId = UUID.randomUUID().toString();

        InputStream inputStream = file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("images")
                            .object(objectId)
                                .stream(inputStream,inputStream.available(), -1)
                        .contentType("image/png")
                        .build()
        );

        uploadRepository.insert(objectId);
    }

    public GetObjectResponse getImage(String id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder().bucket("images").object(id).build());
    }
}
