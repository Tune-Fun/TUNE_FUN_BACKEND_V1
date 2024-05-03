package com.tune_fun.v1.external.aws.s3;

import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Template {

    private final S3Client s3Client;

    public PutObjectResponse uploadMultipartFile(MultipartFile multipartFile, String s3BucketName, String rootPath, @Nullable Tagging tagging) throws IOException, NoSuchAlgorithmException {
        String key = getKey(rootPath, multipartFile);
        return uploadMultipartFile(multipartFile, s3BucketName, tagging, key);
    }

    public PutObjectResponse uploadMultipartFile(MultipartFile multipartFile, String s3BucketName, @Nullable Tagging tagging, String key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(key)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .tagging(tagging)
                .build();

        RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);

        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(s3BucketName).key(key));
        log.info("url : {}", url);

        return putObjectResponse;
    }

    public void deleteObject(@NotNull final String s3BucketName, @NotNull final String key) {

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3BucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);

    }

    public ListObjectsResponse listObjects(String s3BucketName, String rootPath) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(s3BucketName)
                .prefix(rootPath)
                .build();

        return s3Client.listObjects(listObjectsRequest);
    }

    public ResponseBytes<GetObjectResponse> getObject(String s3BucketName, String key) {
        return s3Client.getObjectAsBytes(builder -> builder.bucket(s3BucketName).key(key));
    }

    @NotNull
    public String getKey(String rootPath, MultipartFile multipartFile) throws NoSuchAlgorithmException {
        String generatedFileName = StringUtil.randomAlphanumeric(40);
        log.info("generated file name is {}", generatedFileName);

        String extractedExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        assert extractedExtension != null;

        String extension = extractedExtension.isEmpty() ?
                "jpeg" : extractedExtension;
        log.info("extension is {}", extension);

        return rootPath + "/" + generatedFileName + Constants.DOT + extension;
    }

    public String getUrl(String s3BucketName, String key) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(s3BucketName).key(key)).toString();
    }

    public Tag getTag(String key, String value) {
        return Tag.builder().key(key).value(value).build();
    }

    public Tagging getTagging(String key, String value) {
        return Tagging.builder().tagSet(getTag(key, value)).build();
    }

    public Tagging getTagging(Map<String, String> map) {
        BiFunction<Tagging, Tag, Tagging> accumulator = (tagging, tag) -> tagging.toBuilder().tagSet(tag).build();

        return map.entrySet().stream().map(entry ->
                Tag.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build()
        ).reduce(Tagging.builder().build(), accumulator,
                (t1, t2) -> Tagging.builder()
                        .tagSet(t1.tagSet())
                        .tagSet(t2.tagSet())
                        .build()
        );

    }

}
