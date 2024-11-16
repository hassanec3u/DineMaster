package com.example.demo.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class S3ServicePlat {


    @Value("${url}")
    private String url;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    private MinioClient client;

    @PostConstruct
    public void init() {
        this.client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }


    //retourne l'url presignée pour l'upload d'une image de couverture d'un plat
    @SneakyThrows
    public String getUrlPostCoverByIDForPlat(Long id) {
        return this.client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket("evaluation-17")
                        .object("evaluation-cover" + id)
                        .build()
        );
    }


    //retourne l'url presignée pour le téléchargement de l'image de couverture d'un plat
    @SneakyThrows
    public String getUrlGetCoverByIDForPlat(Long id) {
        return this.client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket("evaluation-17")
                        .object("evaluation-cover" + id)
                        .build()
        );
    }
}
