package org.moozeh.haedalspringboot.repository;

import java.util.List;
import java.util.Optional;
import org.moozeh.haedalspringboot.domain.Follow;
import org.moozeh.haedalspringboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    
    List<Follow> findByFollower(User follower);
    
    List<Follow> findByFollowing(User following);
    
    Long countByFollower(User follower);
    
    Long countByFollowing(User following);
}
