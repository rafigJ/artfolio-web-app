package ru.vsu.cs.artfolio.mapper.wrappers;

public record UserAdditionalInfo(
        Long postCount,
        Long subscribersCount,
        Long followingCount,
        Long likeCount,
        Boolean isFollowed
) {
}
