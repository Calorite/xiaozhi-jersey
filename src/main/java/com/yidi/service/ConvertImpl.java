package com.yidi.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yidi.entity.Parameter;
import com.yidi.interfactoty.ConvertAdapter;

public class ConvertImpl implements ConvertAdapter {

	@Override
	public String Set2StringAdapter(Set<String> set1) {
		String str1="";
		for(String item:set1) {
			if(str1.equals("")) {
				str1=item;
			}else {
				str1=str1+","+item;
			}
		}
		return str1;
	}

	@Override
	public Set<String> String2SetAdapter(String str1) {
		Set<String> set1=new HashSet<String>();
		String[] array=str1.split(",");
		for(String item:array) {
			set1.add(item);
		}
		return set1;
	}

	@Override
	public Map<Integer, Parameter> Set2map(Set<Integer> set1, Map<Integer, Parameter> map1) {
		Map<Integer,Parameter> newmap=new HashMap<>();
		for(Integer i:set1) {
			newmap.put(i,map1.get(i));
		}
		return newmap;
	}

	@Override
	public Map<Integer, Parameter> strSet2map(Set<String> set1, Map<Integer, Parameter> map1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> String2intSet(String str1) {
		Set<Integer> set1=new HashSet<>();
		String[] array=str1.split(",");
		for(String item:array) {
			set1.add(Integer.valueOf(item));
		}
		return set1;
	}

	@Override
	public Set<Parameter> Map2paramterSet(Map<Integer, Parameter> map) {
		Set<Parameter> set1=new HashSet<>();
		for(Parameter p:map.values()) {
			set1.add(p);
		}
		return set1;
	}

	@Override
	public Set<Integer> MapParameter2intset(Map<Parameter, Integer> map) {
		Set<Integer> set=new HashSet<>();
		for (Parameter iterable_element : map.keySet()) {
			set.add(iterable_element.getParameterid());
		}
		return set;
	}

	@Override
	public String intSet2String(Set<Integer> set2) {
		String str="";
		for(Integer id:set2){
			if (str.equals("")) {
				str=String.valueOf(id);
			}else {
				str=str+","+String.valueOf(id);
			}
		}
		return str;
	}

}
