package com.flab.woowahaneats.domain.rider.event;

import com.flab.woowahaneats.domain.rider.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderEventHandler {

    private final RiderService riderService;
}
