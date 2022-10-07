package com.oj.onlinejudge.service.impl.problems;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Problem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.problems.ProblemListService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemListServiceImpl implements ProblemListService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Override
    public JSONObject getProblemListOverview(Integer page) {

        IPage<Problem> problemIPage = new Page<>(page, 10);

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("problemKey");
        List<Problem> problems = problemMapper.selectPage(problemIPage, queryWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> problemList = new ArrayList<>();

        for (Problem p : problems) {
            JSONObject problem = new JSONObject();
            problem.put("problemKey", p.getProblemkey());
            problem.put("problemName", p.getProblemname());
            problem.put("problemSource",p.getSource());

            double acAttempts = 0, totAttempts = 0;
            QueryWrapper<Submission> attemptWrapper = new QueryWrapper<>();
            attemptWrapper.eq("problemkey", Integer.toString(p.getProblemkey()));
            List<Submission> attemptEntries = submissionMapper.selectList(attemptWrapper);
            totAttempts = attemptEntries.size();
            if(totAttempts != 0){
                for (Submission s : attemptEntries) {
                    if (s.getResult().equals("Accepted")) {
                        acAttempts++;
                    }
                }
                problem.put("AcceptedPct", (int)(100 * (acAttempts)/(totAttempts)));
            }else {
                problem.put("AcceptedPct", 0);
            }

            String tagStr = p.getTag();
            String[] tags = tagStr.split(" ");
            problem.put("problemTags", tags[0]);

            problemList.add(problem);
        }

        ret.put("problemList", problemList);
        return ret;
    }
}
