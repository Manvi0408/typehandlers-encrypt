package com.womensafety.userservice.repository;

import com.womensafety.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role = :role")
    UserRole findByUserIdAndRole(@Param("userId") Long userId, @Param("role") UserRole.Role role);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.role = :role")
    List<UserRole> findByRole(@Param("role") UserRole.Role role);
    
    void deleteByUserIdAndRole(Long userId, UserRole.Role role);
    
    boolean existsByUserIdAndRole(Long userId, UserRole.Role role);
}