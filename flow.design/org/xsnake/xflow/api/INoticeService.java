package org.xsnake.xflow.api;

import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;

public interface INoticeService {

	void noticeOnCreateTask(Participant participant, ProcessInstance processInstance, Task task);
	
	void noticeOnProcessComplete(ProcessInstance processInstance);
	
	void noticeOnProcessDestory(ProcessInstance processInstance);
}
