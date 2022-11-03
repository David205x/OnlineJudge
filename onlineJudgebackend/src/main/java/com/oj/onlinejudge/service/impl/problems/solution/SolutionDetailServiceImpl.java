package com.oj.onlinejudge.service.impl.problems.solution;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.SolutionMapper;
import com.oj.onlinejudge.pojo.Problem;
import com.oj.onlinejudge.pojo.Solution;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.problems.solution.SolutionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SolutionDetailServiceImpl implements SolutionDetailService {

    @Autowired
    private SolutionMapper solutionMapper;

    @Override
    public JSONObject getSolutionDetails(String solutionKey) {

        QueryWrapper<Solution> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("solutionKey", solutionKey);
        List<Solution> solutions = solutionMapper.selectList(queryWrapper);

        if (solutions.size() != 1) {
            Logger.titleLogger("SOLUTION DETAILS");
            Logger.basicLogger("solutionKey conflict(s)!");
            Logger.placeholderLogger();
            return null;
        }

        Solution s = solutions.get(0);

        JSONObject ret = new JSONObject();

        ret.put("submissionKey", s.getSolutionkey());
        ret.put("userKey", s.getUserkey());
        ret.put("userName", s.getUsername());
        ret.put("date", s.getTime());
        ret.put("language", s.getLanguage());
        ret.put("content", s.getContent());

        return ret;
    }

}
