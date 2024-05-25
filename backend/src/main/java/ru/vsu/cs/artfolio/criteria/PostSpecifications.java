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

public class PostSpecifications {
    public static Specification<PostEntity> nameContainsIgnoreCase(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<PostEntity> sortByCreateTime() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return null;
        };
    }

    public static Specification<PostEntity> sortByLikeCount() {
        return (root, query, criteriaBuilder) -> {
            // Join с таблицей лайков
            Join<PostEntity, LikeEntity> likesJoin = root.join("likes", JoinType.LEFT);

            // Группировка по постам
            query.groupBy(root.get("id"));

            // Подсчет количества лайков
            Expression<Long> likeCount = criteriaBuilder.count(likesJoin.get("id"));

            // Сортировка по количеству лайков (убывание) и времени создания (убывание)
            query.orderBy(
                    criteriaBuilder.desc(likeCount),
                    criteriaBuilder.desc(root.get("createTime"))
            );

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<PostEntity> postsByFollowedUsers(UserEntity subscriber) {
        return (root, query, criteriaBuilder) -> {
            Join<PostEntity, UserEntity> postOwnerJoin = root.join("owner", JoinType.INNER);
            Join<UserEntity, FollowEntity> followJoin = postOwnerJoin.join("followers", JoinType.INNER);

            query.orderBy(criteriaBuilder.desc(root.get("createTime")));

            return criteriaBuilder.equal(followJoin.get("subscriber"), subscriber);
        };
    }


}
