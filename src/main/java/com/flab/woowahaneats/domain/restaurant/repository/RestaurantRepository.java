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
   Optional<Restaurant> findFirstByNameContaining(String name);

   List<Restaurant> findAllByApprovalStatus(RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findByIdAndApprovalStatus(Long id, RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findFirstByNameContainingAndApprovalStatus(String name, RestaurantApprovalStatus approvalStatus);

   List<Restaurant> findAllByCategoryAndApprovalStatus(RestaurantCategory category, RestaurantApprovalStatus approvalStatus);

   @Query("SELECT r FROM Restaurant r " +
           "WHERE r.approvalStatus = 'APPROVED' " +
           "AND (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(r.location.latitude)) " +
           "* COS(RADIANS(r.location.longitude) - RADIANS(:longitude)) " +
           "+ SIN(RADIANS(:latitude)) * SIN(RADIANS(r.location.latitude)))) <= :radius")
   List<Restaurant> findAllWithinRadius(
           @Param("latitude") double latitude,
           @Param("longitude") double longitude,
           @Param("radius") double radius
   );
}
