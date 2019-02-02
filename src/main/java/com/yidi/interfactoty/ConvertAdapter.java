package com.yidi.interfactoty;

import java.util.Map;
import java.util.Set;

import com.yidi.entity.Parameter;

public interface ConvertAdapter {
	String Set2StringAdapter(Set<String> set1);
	String intSet2String(Set<Integer> set2);
	Set String2SetAdapter(String str1);
	Map<Integer,Parameter> Set2map(Set<Integer> set1,Map<Integer,Parameter> map1);
	Map<Integer,Parameter> strSet2map(Set<String> set1,Map<Integer,Parameter> map1);
	Set<Integer> String2intSet(String str1);
	Set<Parameter> Map2paramterSet(Map<Integer,Parameter> map);
	Set<Integer> MapParameter2intset(Map<Parameter,Integer> map);
}
