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
public class Submission {
    @TableId(type = IdType.AUTO)
    private Integer submissionkey;
    private Integer userkey;
    private Timestamp time;
    private String result;
    private Integer runtime;
    private String language;
}
