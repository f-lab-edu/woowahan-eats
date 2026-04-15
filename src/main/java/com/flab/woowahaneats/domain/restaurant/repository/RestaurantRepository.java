package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantApprovalStatus;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
   Optional<Restaurant> findFirstByNameContaining(String name);

   List<Restaurant> findAllByApprovalStatus(RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findByIdAndApprovalStatus(Long id, RestaurantApprovalStatus approvalStatus);

   Optional<Restaurant> findFirstByNameContainingAndApprovalStatus(String name, RestaurantApprovalStatus approvalStatus);

   List<Restaurant> findAllByCategoryAndApprovalStatus(RestaurantCategory category, RestaurantApprovalStatus approvalStatus);
}
