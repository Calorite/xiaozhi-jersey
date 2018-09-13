package com.yidi.service;

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
		// TODO Auto-generated method stub
		return null;
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

}
