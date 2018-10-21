package com.yidi.interfactoty;

import java.util.List;
import java.util.Set;

import com.yidi.entity.DialogueRecord;
import com.yidi.entity.ParameterSolution;
import com.yidi.entity.PetInfo;

public interface SpecialprocessFactory {
	List<PetInfo> getuserpetType(String username);
	boolean insertpetName(String name,String username);
	boolean insertBirthday(String birthday,String username);
	boolean insertSex(String sexinfo,String username);
	PetInfo ageinfocheck(String name, String username);
	boolean ageProcess(ParameterSolution ps,PetInfo pet);
	boolean sexProcess(ParameterSolution ps,PetInfo pet);
	int CheckInDB(String name,String usename);
}
