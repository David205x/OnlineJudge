package com.oj.onlinejudge.service.impl.problems;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.pojo.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class ProblemFilter implements com.oj.onlinejudge.service.problems.ProblemFilter {

    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public Map<String, String> getProblemByKey(Integer problemKey) {

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problemkey", Integer.toString(problemKey));
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        if (problemEntries.size() != 1) {
            System.out.println("problemKey conflict(s)!");
            return null;
        }

        HashMap<String, String> retMap = new HashMap<>();
        retMap.put("problemKey", Integer.toString(problemKey));
        retMap.put("problemBody",  problemEntries.get(0).getDescription());

        return retMap;
    }

    @Override
    public Map<String, String> getProblemByName(String problemName) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("problemname", problemName);
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        HashMap<String, String> retMap = new HashMap<>();

        if (problemEntries.size() == 0) {
            System.out.println("no matched problem(s)!");
            return retMap;
        }

        for (Problem p : problemEntries) {
            retMap.put("problemKey", Integer.toString(p.getProblemkey()));
            retMap.put("problemBody", p.getDescription());
        }

        return retMap;
    }

    @Override
    public Map<String, String> getProblemByTag(String problemTag, Integer tagCount){

        // TODO: Change this with methods in ProblemParser
        String[] tags = problemTag.split("~~");
        ArrayList<String> queryTags = new ArrayList<>();
        Collections.addAll(queryTags, tags);

        System.out.println(queryTags);

        // TODO: Add multi-tag support, cross-comparing between different result sets
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag", queryTags.get(0));
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        HashMap<String, String> retMap = new HashMap<>();

        if (problemEntries.size() == 0) {
            System.out.println("no problem found with the provided tag(s)!");
            return retMap;
        }

        for (Problem p : problemEntries) {
            retMap.put("problemKey", Integer.toString(p.getProblemkey()));
            retMap.put("problemBody", p.getDescription());
        }

        return retMap;
    }
}
