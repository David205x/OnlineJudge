package com.oj.onlinejudge.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution {
    @TableId(type = IdType.AUTO)
    private Integer solutionkey;
    private Integer problemkey;
    private Integer userkey;
    private String username;
    private Date time;
    private String title;
    private String overview;
    private String content;
}