
package com.cts.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Redemption;
import com.cts.entity.User;

public interface RedemptionRepository extends JpaRepository<Redemption, Long> {
    List<Redemption> findByUser(User user);
    
    List<Redemption> findByUserId(Long userId);
    // or with sorting:
    List<Redemption> findByUserIdOrderByDateDesc(Long userId);
}
