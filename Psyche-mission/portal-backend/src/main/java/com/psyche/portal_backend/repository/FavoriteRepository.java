package com.psyche.portal_backend.repository;

import com.psyche.portal_backend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUsername(String username);
    void deleteByUsername(String username);
}
