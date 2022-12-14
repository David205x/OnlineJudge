package com.oj.onlinejudge.service.impl.problems;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Problem;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.problems.ProblemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProblemDetailServiceImpl implements ProblemDetailService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Override
    public Map<String, String> getProblemDetails(Integer problemKey) {
        // Shown in problem details page, contains detailed problem info.

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problemkey", problemKey);
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        if (problemEntries.size() != 1) {
            Logger.titleLogger("PROBLEM DETAILS");
            Logger.basicLogger("problemKey conflict(s)!");
            Logger.placeholderLogger();
            return null;
        }

        String tagStr = problemEntries.get(0).getTag();
        String[] tags = tagStr.split(" ");

        HashMap<String, String> retMap = new HashMap<>();
        retMap.put("problemKey", Integer.toString(problemKey));
        retMap.put("problemBody",  problemEntries.get(0).getDescription());
        retMap.put("problemName", problemEntries.get(0).getProblemname());
        retMap.put("problemTimeLimit", Integer.toString(problemEntries.get(0).getTimelimit()));
        retMap.put("problemMemoryLimit", Integer.toString(problemEntries.get(0).getMemorylimit()));
        retMap.put("problemDifficulty",  tags[0]);

        int acAttempts = 0, totAttempts = 0;
        QueryWrapper<Submission> attemptWrapper = new QueryWrapper<>();
        attemptWrapper.eq("problemkey", Integer.toString(problemKey));
        List<Submission> attemptEntries = submissionMapper.selectList(attemptWrapper);
        totAttempts = attemptEntries.size();
        for (Submission s : attemptEntries) {
            if (s.getResult().equals("Accepted")) {
                acAttempts++;
            }
        }

        retMap.put("acceptedAttempts",  Integer.toString(acAttempts));
        retMap.put("totalAttempts",  Integer.toString(totAttempts));

        JSONObject tagJson = new JSONObject();
        for (int i = 1; i < tags.length; i++) {
            tagJson.put("tag" + i, tags[i]);
        }

        retMap.put("problemTags", tagJson.toJSONString());

        return retMap;
    }

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
        retMap.put("problemName",  problemEntries.get(0).getProblemname());
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
            retMap.put("problemName",  p.getProblemname());
        }

        return retMap;
    }

    @Override
    public Map<String, String> getProblemByTag(String problemTag, Integer tagCount){

        String[] tags = problemTag.split(" ");
        ArrayList<String> queryTags = new ArrayList<>();
        Collections.addAll(queryTags, tags);

        System.out.println(queryTags);

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("tag", problemTag);
        List<Problem> problemEntries = problemMapper.selectList(queryWrapper);

        HashMap<String, String> retMap = new HashMap<>();

        if (problemEntries.size() == 0) {
            System.out.println("no problem found with the provided tag(s)!");
            return retMap;
        }

        for (Problem p : problemEntries) {
            retMap.put("problemKey", Integer.toString(p.getProblemkey()));
            retMap.put("problemBody", p.getDescription());
            retMap.put("problemName",  p.getProblemname());
        }

        return retMap;
    }

}
