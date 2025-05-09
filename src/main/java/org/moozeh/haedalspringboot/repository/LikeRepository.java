package org.moozeh.haedalspringboot.repository;

import java.util.List;
import java.util.Optional;
import org.moozeh.haedalspringboot.domain.Like;
import org.moozeh.haedalspringboot.domain.Post;
import org.moozeh.haedalspringboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Long countByPost(Post post);
    
    List<Like> findByPost(Post post);
    
    Optional<Like> findByUserAndPost(User user, Post post);
    
    boolean existsByUserAndPost(User user, Post post);
}
