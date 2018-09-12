package com.yidi.interfactoty;

import java.util.Map;

import com.yidi.entity.Parameter;
import com.yidi.entity.ReturnInfo;

public interface TextInfoBytypeFactory {
	ReturnInfo dogprocess(String recivedtext,Map<Integer, Parameter> thisinitalparameters);
	ReturnInfo catprocess(String recivedtext,Map<Integer, Parameter> thisinitalparameters);
}
