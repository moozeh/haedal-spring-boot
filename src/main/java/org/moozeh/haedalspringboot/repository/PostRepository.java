package org.moozeh.haedalspringboot.repository;

import java.util.List;
import org.moozeh.haedalspringboot.domain.Post;
import org.moozeh.haedalspringboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Long countByUser(User user);
    
    List<Post> findByUser(User user);
}
