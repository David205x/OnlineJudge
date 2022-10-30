package com.oj.onlinejudge.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chatting {
    @TableId(type = IdType.AUTO)
    private Integer chatkey;
    private Integer senderkey;
    private String sendername;
    private Integer receiverkey;
    private String receivername;
    private String content;
    private Timestamp time;
    private String state;
}