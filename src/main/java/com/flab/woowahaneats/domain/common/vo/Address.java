package com.flab.woowahaneats.domain.common.vo;

public record Address (
     String province, // 시/도
     String city, // 시/군/구
     String district, // 읍/면/동
     String village, // 리
     String detail // 상세 주소
){}
