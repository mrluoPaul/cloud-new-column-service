package com.mrluo.cloud.common.utils.localmap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mrluo.cloud.common.defs.NewsDefs;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class NewsUserDetailDTO extends User {
    private final NewsUserSession user;

    @JsonCreator
    public NewsUserDetailDTO(NewsUserSession user) {
        this(user.getUsername(), UUID.randomUUID().toString(), // should not store and use password
                true, true, true, true, user);
    }

    public NewsUserDetailDTO(
            String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, NewsUserSession user) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, new ArrayList<>());
        this.user = user;
    }
}
