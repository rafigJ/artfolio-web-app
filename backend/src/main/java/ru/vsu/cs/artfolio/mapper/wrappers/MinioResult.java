package ru.vsu.cs.artfolio.mapper.wrappers;

/**
 * Обертка над результатом, который возвращает MinioClient
 * @param name - уникальное имя файла
 * @param contentType - тип файла
 */
public record MinioResult(String name, String contentType) {
}