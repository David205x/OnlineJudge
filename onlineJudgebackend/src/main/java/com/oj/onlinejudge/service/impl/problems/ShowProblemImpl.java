package com.oj.onlinejudge.service.impl.problems;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.pojo.Problem;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.problems.ShowProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER

@Service
public class ShowProblemImpl implements ShowProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public Map<String, String> getProblem(String problemKey, String extraInfo) {

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problemkey", problemKey);
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        if (problemEntries.size() != 1) {
            throw new RuntimeException("problemKey conflict!");
        }
        if (!extraInfo.isEmpty()) {
            extraInfo = "success";
        }

        HashMap<String, String> retMap = new HashMap<>();
        retMap.put("problemKey", problemKey);
        retMap.put("problemBody",  problemEntries.get(0).getDescription());
        System.out.println(retMap);
        return retMap;
    }
}
