package org.moozeh.haedalspringboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
    
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
