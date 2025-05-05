package com.aabhi.pasman.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginDto {
    private String email;
    private String password;

}
