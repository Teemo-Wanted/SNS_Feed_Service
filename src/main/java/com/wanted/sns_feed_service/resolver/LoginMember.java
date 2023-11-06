package com.wanted.sns_feed_service.resolver;

import com.wanted.sns_feed_service.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {
  private Long id;
  private String account;
  private String accessToken;
  public static LoginMember of(Member member){
    return new LoginMember(member.getId(),member.getAccount(),member.getAccessToken());
  }
}
