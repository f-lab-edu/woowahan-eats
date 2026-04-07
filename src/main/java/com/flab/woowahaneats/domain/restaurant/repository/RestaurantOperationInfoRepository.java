package com.flab.woowahaneats.domain.restaurant.repository;

import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOperationInfoRepository extends JpaRepository<RestaurantOperationInfo, Long> {
}
