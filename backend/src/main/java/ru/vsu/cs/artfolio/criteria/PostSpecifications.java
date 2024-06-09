package ru.vsu.cs.artfolio.criteria;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.vsu.cs.artfolio.entity.FollowEntity;
import ru.vsu.cs.artfolio.entity.LikeEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.UUID;

public class PostSpecifications {

    public static Specification<PostEntity> nameContainsIgnoreCaseSortByCreateTime(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"),
                    criteriaBuilder.isFalse(root.get("deleted"))
            );
        };
    }

    public static Specification<PostEntity> byOwnerId(UUID ownerID) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("ownerId"), ownerID.toString()),
                    criteriaBuilder.isFalse(root.get("deleted"))
            );
        };
    }

    public static Specification<PostEntity> sortByCreateTime() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaBuilder.isFalse(root.get("deleted"));
        };
    }

    public static Specification<PostEntity> sortByLikeCount() {
        return (root, query, criteriaBuilder) -> {
            // Join with the likes table
            Join<PostEntity, LikeEntity> likesJoin = root.join("likes", JoinType.LEFT);

            // Group by posts
            query.groupBy(root.get("id"));

            // Count the number of likes
            Expression<Long> likeCount = criteriaBuilder.count(likesJoin.get("id"));

            // Order by the number of likes (descending) and creation time (descending)
            query.orderBy(
                    criteriaBuilder.desc(likeCount),
                    criteriaBuilder.desc(root.get("createTime"))
            );

            return criteriaBuilder.and(
                    criteriaBuilder.conjunction(),
                    criteriaBuilder.isFalse(root.get("deleted"))
            );
        };
    }

    public static Specification<PostEntity> postsByFollowedUsers(UserEntity subscriber) {
        return (root, query, criteriaBuilder) -> {
            Join<PostEntity, UserEntity> postOwnerJoin = root.join("owner", JoinType.INNER);
            Join<UserEntity, FollowEntity> followJoin = postOwnerJoin.join("followers", JoinType.INNER);

            query.orderBy(criteriaBuilder.desc(root.get("createTime")));

            return criteriaBuilder.and(
                    criteriaBuilder.equal(followJoin.get("subscriber"), subscriber),
                    criteriaBuilder.isFalse(root.get("deleted"))
            );
        };
    }
}
