package com.yidi.interfactoty;

import java.util.List;
import java.util.Set;

import com.yidi.entity.DialogueRecord;
import com.yidi.entity.PetInfo;

public interface SpecialprocessFactory {
	List<PetInfo> getuserpetType(String username);
	boolean insertpetName(String name,String username);
	boolean ageinfocheck();
	int ageProcess(String text,DialogueRecord lr);
	int sexProcess(String text,DialogueRecord lr);
}
