package com.oj.onlinejudge.service.impl.user.self;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.impl.GenericOjFilter;
import com.oj.onlinejudge.service.user.self.SelfSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SelfSubmissionsServiceImpl extends GenericOjFilter implements SelfSubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;
    private final int entriesPerPage = 12;
    private final SimpleDateFormat submissionTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JSONObject selfSubmissionInfoExtractor(Submission s) {

        JSONObject submission = new JSONObject();
        submission.put("submissionKey", s.getSubmissionkey());
        submission.put("result", s.getResult());
        submission.put("timeUsed", s.getRuntime());
        submission.put("lang", s.getLanguage());
        submission.put("date", submissionTime.format(new Date(s.getTime().getTime())));

        return submission;
    }

    public JSONObject selfSubmissionListGetter(String userKey, String column, int order, int page) {

        IPage<Submission> submissionIPage = new Page<>(page, entriesPerPage);

        if (column.isEmpty()) {
            column = "time";
        }

        QueryWrapper<Submission> submissionsWrapper = new QueryWrapper<>();
        submissionsWrapper.eq("userkey", userKey);
        if (order == 0) {
            submissionsWrapper.orderByDesc(column);
        } else {
            submissionsWrapper.orderByAsc(column);
        }
        List<Submission> submissions = submissionMapper.selectPage(submissionIPage, submissionsWrapper).getRecords();

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> submissionList = new ArrayList<>();

        for (Submission s : submissions) {
            submissionList.add(selfSubmissionInfoExtractor(s));
        }

        ret.put("submissionCount", submissionList.size());
        ret.put("totalPages", submissionMapper.selectCount(submissionsWrapper));
        ret.put("perPage", entriesPerPage);
        ret.put("submissionList", submissionList);

        return ret;

    }

}
