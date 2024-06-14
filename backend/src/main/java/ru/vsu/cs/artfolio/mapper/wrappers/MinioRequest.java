package ru.vsu.cs.artfolio.mapper.wrappers;

import java.io.InputStream;

/**
 * Должен использоваться в MinioService
 * Пока не все методы принимают его.
 */
public record MinioRequest(InputStream inputStream, String contentType) {
    public static MinioRequest of(InputStream is, String contentType) {
        return new MinioRequest(is, contentType);
    }
}
