package com.side.rest.login.discord.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2RequestDto {

    private String code;
    private String error;
    private String errorDescription;
    private String state;
}
