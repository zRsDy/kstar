package com.ibm.kstar.service;

import java.util.List;

import org.xsnake.xflow.api.Participant;

public interface IParticipantService {

	List<Participant> getCreatorDirectLeadership(String id);
	
	List<Participant> getByLevel(String id,int level);
}
