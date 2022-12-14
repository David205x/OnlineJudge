package com.oj.onlinejudge.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friends {
    @TableId(type = IdType.AUTO)
    private Integer userkey;
    private String username;
    private String friends;
}
