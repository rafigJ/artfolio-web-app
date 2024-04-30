package ru.vsu.cs.artfolio.mapper;

import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;

import java.util.ArrayList;
import java.util.List;

public class MediaMapper {

    /**
     * @param mediaFiles - упорядоченный список файлов, которые необходимо преобразовать в сущность
     * @param post       - публикация, к которому будут ссылаться данные сущности
     */
    public static List<MediaFileEntity> toEntityList(List<MinioResult> mediaFiles, PostEntity post) {
        List<MediaFileEntity> entities = new ArrayList<>();
        int pos = 0;
        for (MinioResult mediaFile : mediaFiles) {
            entities.add(MediaFileEntity.builder()
                    .post(post)
                    .fileName(mediaFile.name())
                    .position(pos++)
                    .type(mediaFile.contentType())
                    .build());
        }
        return entities;
    }
}

