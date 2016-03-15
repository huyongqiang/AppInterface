package com.sniyve.ai;


import java.lang.reflect.Method;
import java.util.List;

public class MappingPath {

	private String path;

	private String instanceName;

	private Method method;

	private List<MappingPath> mappingPathList;

	public MappingPath(){}

	public MappingPath(String path, String instanceName, Method method){
		this.path = path;
		this.instanceName = instanceName;
		this.method = method;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
