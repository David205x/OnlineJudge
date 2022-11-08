package com.oj.onlinejudge.service.impl.problems.solution;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.mapper.SolutionMapper;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.Solution;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.impl.GenericOjFilter;
import com.oj.onlinejudge.service.problems.solution.SolutionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class SolutionListServiceImpl extends GenericOjFilter implements SolutionListService{

    @Autowired
    private SolutionMapper solutionMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProblemMapper problemMapper;
    private final int entriesPerPage = 8;

    private JSONObject solutionInfoExtractor(Solution s) {

        JSONObject solution = new JSONObject();

        solution.put("solutionKey", s.getSolutionkey());
        solution.put("problemKey", s.getProblemkey());
        solution.put("userKey", s.getUserkey());
        solution.put("userName", s.getUsername());
        solution.put("problemName", problemMapper.selectById(s.getProblemkey()).getProblemname());
        solution.put("language", s.getLanguage());
        solution.put("date", s.getTime());
        solution.put("title",s.getTitle());
        String contentOverview =  s.getContent().length() > 16 ? s.getContent().substring(0, 16) + "..." : s.getContent();
        solution.put("contentOverview", contentOverview);

        return solution;
    }

    @Override
    public boolean updateSolution(Integer solutionKey, String language, String problemKey, String userKey, String content) {

        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("id", Integer.parseInt(userKey));
        List<User> Users = userMapper.selectList(userWrapper);

        System.out.println("?????");
        System.out.println(solutionMapper.updateById(new Solution(
                solutionKey,
                Integer.parseInt(problemKey),
                Integer.parseInt(userKey),
                Users.get(0).getUsername(),
                new Date(System.currentTimeMillis()),
                "",
                "",
                content,
                language)
        ));
        return solutionMapper.updateById(new Solution(
                solutionKey,
                Integer.parseInt(problemKey),
                Integer.parseInt(userKey),
                Users.get(0).getUsername(),
                new Date(System.currentTimeMillis()),
                "",
                "",
                content,
                language)
        ) != 0;
    }
    @Override
    public boolean addSolution(String language, String problemKey, String userKey, String title, String content) {

        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("id", Integer.parseInt(userKey));
        List<User> Users = userMapper.selectList(userWrapper);

        return solutionMapper.insert(new Solution(
                null,
                Integer.parseInt(problemKey),
                Integer.parseInt(userKey),
                Users.get(0).getUsername(),
                new Date(System.currentTimeMillis()),
                title,
                "",
                content,
                language)
        ) != 0;
    }

    @Override
    public boolean deleteSolution(String solutionKey) {
        return solutionMapper.deleteById(Integer.parseInt(solutionKey)) != 0;
    }

    @Override
    public JSONObject randomSolutionGetter() {
        JSONObject ret = new JSONObject();
        Solution randomSolution = solutionMapper.selectById(
                new Random().nextInt(
                        Math.toIntExact(solutionMapper.selectCount(null))
                ) + 1);
        ret.put("solution", randomSolution);
        ret.put("problemName",problemMapper.selectById(randomSolution.getProblemkey()).getProblemname());
        return ret;

    }

    @Override
    public JSONObject solutionListGetterForOnes(String userKey, Integer page) {
        IPage<Solution> solutionIPage = new Page<>(page, entriesPerPage);

        QueryWrapper<Solution> solutionWrapper = new QueryWrapper<>();
        solutionWrapper.eq("userKey", userKey);
        solutionWrapper.orderByDesc("solutionKey");
        List<Solution> solutions = solutionMapper.selectPage(solutionIPage, solutionWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> problemList = new ArrayList<>();

        for (Solution s : solutions) {
            problemList.add(solutionInfoExtractor(s));
        }

        ret.put("solutionsCount", solutions.size());
        ret.put("totalPages", solutionMapper.selectCount(solutionWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("solutionList", problemList);

        return ret;
    }

    @Override
    public JSONObject solutionListGetter(String problemKey, Integer page) {

        IPage<Solution> solutionIPage = new Page<>(page, entriesPerPage);

        QueryWrapper<Solution> solutionWrapper = new QueryWrapper<>();
        solutionWrapper.eq("problemkey", problemKey);
        solutionWrapper.orderByDesc("solutionKey");
        List<Solution> solutions = solutionMapper.selectPage(solutionIPage, solutionWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> problemList = new ArrayList<>();

        for (Solution s : solutions) {
            problemList.add(solutionInfoExtractor(s));
        }

        ret.put("solutionsCount", solutions.size());
        ret.put("totalPages", solutionMapper.selectCount(solutionWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("solutionList", problemList);

        return ret;
    }

    @Override
    public Set<Integer> getFullSolutionList() {
        return null;
    }

}
