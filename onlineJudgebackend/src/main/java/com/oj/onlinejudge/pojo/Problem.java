package com.oj.onlinejudge.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    @TableId(type = IdType.AUTO)
    private Integer problemkey;
    private String problemname;
    private String source;
    private String description;
    private String sampleio;
    private String tag;
}
