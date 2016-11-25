package com.aerse.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class CronTimer {

	private static final Logger LOG = Logger.getLogger(CronTimer.class);
	private PeriodTimer timer;
	private Map<Runnable, String> tasks;
	private final Map<String, CronTimerTask> cronTasks = new HashMap<>();
	private String taskNames = "";
	private boolean enabled;

	@PostConstruct
	public void start() {
		if (!enabled) {
			LOG.info("timer is not enabled");
		}
		for (Entry<Runnable, String> cur : tasks.entrySet()) {
			CronTimerTask task = new CronTimerTask(cur.getValue(), cur.getKey(), timer);
			if (enabled) {
				task.schedule();
			}
			cronTasks.put(task.getTaskName(), task);
			taskNames += task.getTaskName() + "<br/>";
		}
	}

	@Required
	public void setTimer(PeriodTimer timer) {
		this.timer = timer;
	}

	@Required
	public void setTasks(Map<Runnable, String> tasks) {
		this.tasks = tasks;
	}

	@Required
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@ManagedAttribute
	public String getTaskNames() {
		return taskNames;
	}

	@ManagedOperation(description = "immediately execute task")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "taskName", description = "name of java class") })
	public String execute(String taskClass) {
		CronTimerTask task = cronTasks.get(taskClass);
		if (task == null) {
			return "task not found: " + taskClass;
		}
		CronTimerTask copy = new CronTimerTask(task);
		copy.schedule(0l);
		return "OK";
	}

}
