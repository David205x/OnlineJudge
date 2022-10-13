package com.oj.onlinejudge.service.impl.problems;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.impl.GenericFilterService;
import com.oj.onlinejudge.service.problems.SubmissionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SubmissionListListServiceImpl implements SubmissionListService, GenericFilterService {

    @Autowired
    private SubmissionMapper submissionMapper;
    private final int entriesPerPage = 10;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    private JSONObject submissionInfoExtractor(Submission s) {

        JSONObject submission = new JSONObject();
        submission.put("submissionKey", s.getSubmissionkey());
        submission.put("userKey", s.getUserkey());
        submission.put("result", s.getResult());
        submission.put("timeUsed", s.getRuntime());
        submission.put("lang", s.getLanguage());
        submission.put("date", s.getTime());

        return submission;
    }

    @Override
    public JSONObject masterSubmissionListGetter(String problemKey,
                                                 String userName,
                                                 String result,
                                                 String lang,
                                                 int page) {

        Logger.titleLogger("SUBMISSION FILTER");

        boolean enableUserFilter = !userName.isEmpty();
        boolean enableResultFilter = !result.isEmpty();
        boolean enableLangFilter = !lang.isEmpty();

        JSONObject ret = new JSONObject();

        if (!enableUserFilter && !enableResultFilter && !enableLangFilter ) {
            return submissionListGetter(problemKey, page);
        }
        ArrayList<JSONObject> submissionList = new ArrayList<>();

        IPage<Submission> submissionIPage = new Page<>(page, entriesPerPage);
        QueryWrapper<Submission> masterWrapper = new QueryWrapper<>();

        Set<Integer> base = new HashSet<>(getFullSubmissionList());
        if (enableUserFilter) {
            base.retainAll(getProblemListByColumn("userKey", userName));
        }
        if (enableResultFilter) {
            base.retainAll(getProblemListByColumn("result", userName));
        }
        if (enableLangFilter) {
            base.retainAll(getProblemListByColumn("lang", userName));
        }

        if (base.isEmpty()) {
            System.out.println("No matched submissions. ");
            ret.put("submissionCount", -1);
            ret.put("totalPages", 0);
            ret.put("perPage", entriesPerPage);
            ret.put("submissionList", null);
        } else {

            System.out.println("Final set: " + base);

            for (int key : base) {
                masterWrapper.eq("submission", key).or();
            }
            List<Submission> submissions = submissionMapper.selectPage(submissionIPage, masterWrapper).getRecords();

            for (Submission s : submissions) {
                submissionList.add(submissionInfoExtractor(s));
            }

            ret.put("submissionCount", submissions.size());
            ret.put("totalPages", submissionMapper.selectCount(masterWrapper));
            ret.put("perPage", entriesPerPage);
            ret.put("submissionList", submissionList);
        }

        Logger.placeholderLogger();

        return ret;
    }

    @Override
    public Set<Integer> getFullSubmissionList() {

        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("problemKey");
        List<Submission> submissions = submissionMapper.selectList(queryWrapper);

        Set<Integer> ret = new HashSet<>();

        for (Submission s : submissions) {
            ret.add(s.getSubmissionkey());
        }

        return ret;
    }

    @Override
    public JSONObject submissionListGetter(String problemKey, int page) {

        IPage<Submission> submissionIPage = new Page<>(page, entriesPerPage);

        QueryWrapper<Submission> submissionsWrapper = new QueryWrapper<>();
        submissionsWrapper.eq("problemkey", Integer.parseInt(problemKey));
        List<Submission> submissions = submissionMapper.selectPage(submissionIPage, submissionsWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> submissionList = new ArrayList<>();

        for (Submission s : submissions) {
            submissionList.add(submissionInfoExtractor(s));
        }

        ret.put("problemCount", submissionList.size());
        ret.put("totalPages", submissionMapper.selectCount(null));
        ret.put("perPage", entriesPerPage);
        ret.put("problemList", submissionList);

        System.out.println("Getting all submissions.");
        Logger.placeholderLogger();

        return ret;
    }

    @Override
    public Set<Integer> getProblemListByColumn(String columnName, String criteria) {

        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("submissionKey");
        queryWrapper.like(columnName, criteria);
        List<Submission> submissions = submissionMapper.selectList(queryWrapper);

        Set<Integer> ret = new HashSet<>();

        for (Submission s : submissions) {
            ret.add(s.getSubmissionkey());
        }

        return ret;
    }
}
