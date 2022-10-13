package com.oj.onlinejudge.service.checker.utils;

import java.util.ArrayList;

public interface IOHelper {

    public boolean readAll();
    public String getAll();
    public ArrayList<String> getAllByLines();
    public boolean writeAll(String fileContent);

}
