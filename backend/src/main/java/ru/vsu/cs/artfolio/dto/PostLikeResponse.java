package ru.vsu.cs.artfolio.dto;

public record PostLikeResponse(
        Long postId,
        Long likeCount
) { }
