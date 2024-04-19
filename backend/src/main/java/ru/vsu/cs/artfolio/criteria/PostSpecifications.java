package ru.vsu.cs.artfolio.criteria;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.vsu.cs.artfolio.entity.PostEntity;

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
}
