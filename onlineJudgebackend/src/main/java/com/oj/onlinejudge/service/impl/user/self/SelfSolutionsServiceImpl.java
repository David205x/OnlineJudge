package com.oj.onlinejudge.service.impl.user.self;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.SolutionMapper;
import com.oj.onlinejudge.pojo.Solution;
import com.oj.onlinejudge.service.impl.GenericOjFilter;
import com.oj.onlinejudge.service.user.self.SelfSolutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfSolutionsServiceImpl extends GenericOjFilter implements SelfSolutionsService {

    @Autowired
    private SolutionMapper solutionMapper;
    private final int entriesPerPage = 12;
    private final SimpleDateFormat submissionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JSONObject selfSolutionInfoExtractor(Solution s) {

        JSONObject solution = new JSONObject();
        solution.put("solutionKey", s.getSolutionkey());
        solution.put("date", s.getTime());
        String contentOverview = s.getContent().length() > 16 ? s.getContent().substring(0, 16) + "..." : s.getContent();
        solution.put("contentOverview", contentOverview);
        // maybe add a 'VIEWS' column?

        return solution;
    }

    public JSONObject selfSolutionListGetter(String userKey, String column, int order, int page) {

        IPage<Solution> solutionIPage = new Page<>(page, entriesPerPage);

        QueryWrapper<Solution> solutionWrapper = new QueryWrapper<>();
        solutionWrapper.eq("userkey", userKey);
        if (order == 0) {
            solutionWrapper.orderByDesc(column);
        } else {
            solutionWrapper.orderByAsc(column);
        }
        List<Solution> solutions = solutionMapper.selectPage(solutionIPage, solutionWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> problemList = new ArrayList<>();

        for (Solution s : solutions) {
            problemList.add(selfSolutionInfoExtractor(s));
        }

        ret.put("solutionsCount", solutions.size());
        ret.put("totalPages", solutionMapper.selectCount(solutionWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("solutionList", problemList);

        return ret;
    }
}

