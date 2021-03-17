package com.kakaopay.sprinkling.common.interceptor;

import com.kakaopay.sprinkling.domain.RoomId;
import com.kakaopay.sprinkling.domain.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class SprinklingAuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    log.info("[REQ] (AuthInfo) {}, {}, USER_ID : {}, ROOM_ID : {}",
            request.getMethod(),
            request.getRequestURI(),
            UserId.of(request),
            RoomId.of(request));

    return true;
  }

}
