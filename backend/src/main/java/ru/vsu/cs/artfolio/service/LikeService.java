package ru.vsu.cs.artfolio.service;

import java.util.List;
import java.util.UUID;

public interface LikeService {

    void createLike(UUID executorId, Long postId);

    /**
     * Возвращает флаг о том подписан ли пользователь на пост
     * @return true если пользователь поставил лайк на postId, false если лайка нет
     */
    boolean hasLike(UUID userId, Long postId);

    /**
     * @return количество лайков данного поста
     */
    Long getLikeCount(Long postId);

    /**
     * @return общее количество лайков списка постов
     */
    Long getLikeCount(List<Long> postIds);

    /**
     * Нужно для получения количество лайков каждого поста в списке, не теряя последовательность.
     * Например, для списка postIds [1, 3, 9] может вернуть список такого же размера [40, 0, 32],
     * который будет отображать информацию о количестве лайков для поста
     * id 1 -> 40 лайков
     * id 3 -> 0 лайков
     * id 9 -> 32 лайка
     * @return список количества лайков каждого поста. Работает как функция map
     */
    List<Long> getLikeCountsEachPostInList(List<Long> postIds);

    void deleteLike(UUID executorId, Long postId);

}
