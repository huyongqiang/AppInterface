package com.sniyve.ai;

import com.sniyve.ai.MappingPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanglang on 16/3/13.
 */
public class MappingHost {

    private String host;

    private String scheme;

    private Object owner;

    private List<MappingPath> mappingPathList = new ArrayList<MappingPath>();

    public MappingHost(String scheme,String host,Object owner){
        this.scheme = scheme;
        this.host = host;
        this.owner = owner;
    }

    public MappingHost(){}

    public MappingHost(String scheme,String host){
        this(scheme,host,null);
    }

    public void addMappingPath(MappingPath mappingPath){
        this.mappingPathList.add(mappingPath);
    }


    public String getHost() {
        return host;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public void setHost(String host) {
        this.host = host;

    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public List<MappingPath> getMappingPathList() {
        return mappingPathList;
    }

    public void setMappingPathList(List<MappingPath> mappingPathList) {
        this.mappingPathList = mappingPathList;
    }
}
