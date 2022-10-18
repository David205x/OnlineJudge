package com.oj.onlinejudge.service.impl;

import com.oj.onlinejudge.service.Logger;

import java.util.ArrayList;

public class GenericOjFilter {

    public String normalize(String content, int len) {
        if (content.length() <= len) {
            return content;
        } else {
            return content.substring(0, len - 1) + "...";
        }
    }

    public String wrapInQuotes(String content) {
        return "\"" + content + "\"";
    }

    public String sqlQueryBuilder(ArrayList<String> criteria, ArrayList<Integer> restrictions, ArrayList<String> values) {

        if (criteria.size() != restrictions.size() || criteria.size() != values.size()) {
            return null;
        }

        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0; i < criteria.size(); i++) {
            queryBuilder.append(criteria.get(i));
            if (restrictions.get(i) == 0) {
                // eq-str
                queryBuilder.append(" = ").append(wrapInQuotes(values.get(i))).append(" ");
            } else if (restrictions.get(i) == 1) {
                // like-str
                queryBuilder.append(" LIKE ").append(wrapInQuotes("%" + values.get(i) + "%")).append(" ");
            } else {
                // eq-int
                queryBuilder.append(" = ").append(values.get(i)).append(" ");
            }
            if (i != criteria.size() - 1) {
                queryBuilder.append("AND ");
            }
        }
        return queryBuilder.toString();
    }
}
