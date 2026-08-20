package ru.kirillvodu.dorogame.user.infrastructure.persistence.adapters;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.AvatarContent;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.AvatarStorage;
import ru.kirillvodu.dorogame.user.application.exceptions.AvatarDownloadException;
import ru.kirillvodu.dorogame.user.application.exceptions.AvatarUploadException;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AvatarStorageMinioAdapter implements AvatarStorage {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String uploadAvatar(InputStream in, long size, String contentType) {
        String key = UUID.randomUUID().toString();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .contentType(contentType)
                    .stream(in, size, -1)
                    .object(key)
                    .build());
        } catch (Exception e) {
            throw new AvatarUploadException(e.getMessage());
        }
        return key;
    }

    @Override
    public Optional<AvatarContent> downloadAvatar(String avatarKey) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(avatarKey)
                    .build());
            String contentType = response.headers().get("Content-Type");
            long size = Long.parseLong(response.headers().get("Content-Length"));
            return Optional.of(new AvatarContent(response, contentType, size));
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return Optional.empty();
            }
            throw new AvatarDownloadException(e.getMessage());
        } catch (Exception e) {
            throw new AvatarDownloadException(e.getMessage());
        }
    }
}
