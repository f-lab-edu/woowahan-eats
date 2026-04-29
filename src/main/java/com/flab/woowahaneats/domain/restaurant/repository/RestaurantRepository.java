package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantApprovalStatus;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
   Optional<Restaurant> findFirstByOwnerId(Long ownerId);

   Optional<Restaurant> findFirstByNameContaining(String name);

   List<Restaurant> findAllByApprovalStatus(RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findByIdAndApprovalStatus(Long id, RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findFirstByNameContainingAndApprovalStatus(String name, RestaurantApprovalStatus approvalStatus);

   List<Restaurant> findAllByCategoryAndApprovalStatus(RestaurantCategory category, RestaurantApprovalStatus approvalStatus);

   @Query(value = "SELECT r.* FROM restaurants r " +
           "WHERE r.approval_status = 'APPROVED' " +
           "AND ST_DWithin(r.point, " +
           "ST_MakePoint(:longitude, :latitude)::geography, :radius * 1000)",
           nativeQuery = true)
   List<Restaurant> findAllWithinRadius(
           @Param("latitude") double latitude,
           @Param("longitude") double longitude,
           @Param("radius") double radius
   );

   @Query(value = "SELECT r.* FROM restaurants r " +
           "WHERE r.approval_status = 'APPROVED' " +
           "AND r.category = :#{#category.name()} " +
           "AND ST_DWithin(r.point, " +
           "ST_MakePoint(:longitude, :latitude)::geography, :radius * 1000)",
           nativeQuery = true)
   List<Restaurant> findAllByCategoryWithinRadius(
           @Param("category") RestaurantCategory category,
           @Param("latitude") double latitude,
           @Param("longitude") double longitude,
           @Param("radius") double radius
   );
}
