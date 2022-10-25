package com.chattingroom.chattingroom.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    private Integer Id;
    private String userName;
    private String avatarUrl;
}
