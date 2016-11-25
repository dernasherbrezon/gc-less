package com.aerse.util;

import java.util.Date;
import java.util.TimerTask;

class CronTimerTask extends TimerTask {

	private final Runnable impl;
	private final PeriodTimer timer;
	private final CronDate schedule;
	private final String taskName;
	private final String cronSchedule;

	CronTimerTask(String cronSchedule, Runnable impl, PeriodTimer timer) {
		this.timer = timer;
		this.impl = impl;
		this.cronSchedule = cronSchedule;
		this.schedule = new CronDate(cronSchedule);
		this.taskName = impl.getClass().getName();
	}

	CronTimerTask(CronTimerTask copy) {
		this.impl = copy.impl;
		this.timer = copy.timer;
		this.cronSchedule = copy.cronSchedule;
		this.schedule = new CronDate(this.cronSchedule);
		this.taskName = copy.taskName;
	}
	
	@Override
	public void run() {
		impl.run();
		schedule();
	}

	void schedule() {
		Date next = schedule.getNext(System.currentTimeMillis());
		timer.schedule(taskName, new CronTimerTask(this), next);
	}

	void schedule(long delay) {
		timer.schedule(taskName, new TimerTask() {
			@Override
			public void run() {
				CronTimerTask.this.impl.run();
			}
		}, delay);
	}

	String getTaskName() {
		return taskName;
	}
}
