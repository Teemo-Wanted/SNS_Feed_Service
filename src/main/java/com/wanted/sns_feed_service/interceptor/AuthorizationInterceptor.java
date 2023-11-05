package com.wanted.sns_feed_service.interceptor;

import com.wanted.sns_feed_service.jwt.JwtProvider;
import com.wanted.sns_feed_service.member.entity.Member;
import com.wanted.sns_feed_service.member.repository.MemberRepository;
import com.wanted.sns_feed_service.resolver.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
  private final JwtProvider jwtProvider;
  private final MemberRepository memberRepository;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = extractToken(request);
    if(!jwtProvider.verify(token))
      return false;
    Optional<Member> memberOptional=extractMember(token);
    if(memberOptional.isEmpty())
      return false;
    if(!verifyAccessToken(token,memberOptional))
      return false;
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  private String extractToken(HttpServletRequest request){
    return request.getHeader("Authorization");
  }
  private Optional<Member> extractMember(String token){
    return memberRepository.findById((Long) jwtProvider.getClaims(token).get("id"));
  }
  private boolean verifyAccessToken(String token,Optional<Member> memberOptional){
    return token.equals(memberOptional.get().getAccessToken());
  }
}