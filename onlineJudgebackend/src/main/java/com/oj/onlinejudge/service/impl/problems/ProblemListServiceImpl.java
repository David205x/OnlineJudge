package com.oj.onlinejudge.service.impl.problems;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.pojo.Problem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oj.onlinejudge.service.problems.ProblemListService;
import net.minidev.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ProblemListServiceImpl implements ProblemListService {

    private ProblemMapper problemMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Problem> problemIPage =new Page<>(page,10);
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("problemKey");
        List<Problem> problems = problemMapper.selectPage(problemIPage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Problem problem: problems){
            JSONObject item = new JSONObject();
            item.put("problemKey",problem.getProblemkey());
            item.put("problemName",problem.getProblemname());
            items.add(item);
        }
        resp.put("problemList",items);
        return null;
    }
}
