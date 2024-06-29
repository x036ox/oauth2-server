package com.artur.service;

import com.artur.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OidcUserServiceImpl implements OidcUserInfoService{

    @Autowired
    private UserService userService;

    @Override
    public OidcUserInfo loadByUsername(String username) {
        UserEntity userEntity = (UserEntity) userService.loadUserByUsername(username);
        try {
            return OidcUserInfo.builder()
                    .email(userEntity.getEmail())
                    .picture("/user/picture/" + userEntity.getId())
                    .nickname(userEntity.getNickname())
                    .claim("authorities", userEntity.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
