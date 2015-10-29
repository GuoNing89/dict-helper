package com.naive.gn.plugin.dict.action;

import java.util.List;

/**
 * Naive-GN
 * Created by guoning on 15/10/29.
 */
public class DictBean {
    public List<String> translation;
    public Base base;
    public String query;
    public int errorCode;
    public List<Web> web;
}

class Base {
    public List<String> explains;
}

class Web {
    public String key;
    public List<String> value;
}


