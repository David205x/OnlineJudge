package com.oj.onlinejudge.service.impl.problems;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.ProblemMapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.impl.GenericOjFilter;
import com.oj.onlinejudge.service.problems.SubmissionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SubmissionListServiceImpl extends GenericOjFilter implements SubmissionListService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private ProblemMapper problemMapper;

    private final int entriesPerPage = 10;
    private final SimpleDateFormat submissionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JSONObject submissionInfoExtractor(Submission s) {

        JSONObject submission = new JSONObject();
        submission.put("submissionKey", s.getSubmissionkey());
        submission.put("userKey", s.getUserkey());
        submission.put("userName", s.getUsername());
        submission.put("result", s.getResult());
        submission.put("timeUsed", s.getRuntime());
        submission.put("lang", s.getLanguage());
        submission.put("date", submissionTime.format(new Date(s.getTime().getTime())));

        return submission;
    }

    @Override
    public JSONObject masterSubmissionListGetter(String problemKey,
                                                 String userName,
                                                 String result,
                                                 String lang,
                                                 int page) {

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

        ArrayList<String> criteria = new ArrayList<>();
        ArrayList<Integer> restrictions = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        criteria.add("problemKey");
        restrictions.add(0);
        values.add(problemKey);

        if (enableUserFilter) {
            criteria.add("userName");
            restrictions.add(1);
            values.add(userName);
        }
        if (enableResultFilter) {
            criteria.add("result");
            restrictions.add(0);
            values.add(result);
        }
        if (enableLangFilter) {
            criteria.add("language");
            restrictions.add(0);
            values.add(lang);
        }

        String sql = sqlQueryBuilder(criteria, restrictions, values);

        masterWrapper.apply(sql);
        masterWrapper.orderByDesc("time");
        List<Submission> submissions = submissionMapper.selectPage(submissionIPage, masterWrapper).getRecords();

        for (Submission s : submissions) {
            submissionList.add(submissionInfoExtractor(s));
        }

        ret.put("submissionCount", submissions.size());
        ret.put("totalPages", submissionMapper.selectCount(masterWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("submissionList", submissionList);

        return ret;
    }

    @Override
    public Set<Integer> getFullSubmissionList() {

        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("submissionkey");
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
        submissionsWrapper.orderByDesc("time");
        List<Submission> submissions = submissionMapper.selectPage(submissionIPage, submissionsWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> submissionList = new ArrayList<>();

        for (Submission s : submissions) {
            submissionList.add(submissionInfoExtractor(s));
        }


        ret.put("submissionCount", submissionList.size());
        ret.put("totalPages", submissionMapper.selectCount(submissionsWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("submissionList", submissionList);

        return ret;
    }

    @Override
    public JSONObject masterSubmissionListGetterForOnes(String userKey, int page) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("submissionKey");
        queryWrapper.eq("userkey", userKey);
        List<Submission> s = submissionMapper.selectList(queryWrapper);
        Map<Integer, Integer> subTime = new HashMap<>();
        Map<Integer, Submission> submissionMap = new HashMap<>();
        for(Submission submission : s){
            Integer subKey = submission.getProblemkey();
            if(!subTime.containsKey(subKey)){
                subTime.put(subKey, 0);
            }
            subTime.put(subKey, subTime.get(subKey) + 1);
            if(submission.getResult().equals("Accepted")){
                submissionMap.put(subKey, submission);
            }
        }
        for(Submission submission : s){
            Integer subKey = submission.getProblemkey();
            if(!submissionMap.containsKey(subKey)) {
                submissionMap.put(subKey, submission);
            }
        }
        ArrayList<JSONObject> submissionList = new ArrayList<>();
        int pos = (page - 1) * entriesPerPage;
        int cnt = 0;
        for (Integer key : submissionMap.keySet()) {
            if(cnt < pos){
                continue;
            }else if(cnt >= pos + entriesPerPage){
                continue;
            }
            cnt++;
            JSONObject submission = new JSONObject();
            Submission sub = submissionMap.get(key);
            submission.put("result", sub.getResult());
            submission.put("problemKey", sub.getProblemkey());
            submission.put("problemName", problemMapper.selectById(sub.getProblemkey()).getProblemname());
            submission.put("submitTime", subTime.get(key));
            String[] tags = problemMapper.selectById(sub.getProblemkey()).getTag().split(" ");
            submission.put("label", tags);
            submissionList.add(submission);
        }
        JSONObject ret = new JSONObject();
        ret.put("submissionList", submissionList);
        ret.put("totalPages", (submissionMap.size() + entriesPerPage - 1) / entriesPerPage);
        ret.put("perPage", entriesPerPage);
        ret.put("error_message", "success");
        return ret;
    }

    @Override
    public Set<Integer> getProblemListByColumn(String columnName, String value) {

        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("submissionKey");
        queryWrapper.like(columnName, value);

        List<Submission> submissions = submissionMapper.selectList(queryWrapper);

        Set<Integer> ret = new HashSet<>();

        for (Submission s : submissions) {
            ret.add(s.getSubmissionkey());
        }

        return ret;
    }
}
