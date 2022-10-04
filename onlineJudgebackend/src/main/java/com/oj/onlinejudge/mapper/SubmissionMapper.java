package com.oj.onlinejudge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oj.onlinejudge.pojo.Submission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
}
