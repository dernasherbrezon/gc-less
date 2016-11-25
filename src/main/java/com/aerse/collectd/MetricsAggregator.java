package com.aerse.collectd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.collectd.api.ValueList;
import org.collectd.mx.Collector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.aerse.util.SafeRunnable;

@ManagedResource
public class MetricsAggregator implements DestructionAwareBeanPostProcessor {

	private static final Logger LOG = Logger.getLogger(MetricsAggregator.class);
	// private UdpSender sender;
	private Sender defaultSender;
	private long intervalSec;

	private Set<Sender> senders = new HashSet<Sender>();
	private Set<RoutedCollector> pendingInitialization = new HashSet<RoutedCollector>();
	private Map<Collector, ScheduledFuture<?>> collectors = new HashMap<Collector, ScheduledFuture<?>>();
	private ScheduledExecutorService executor;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Sender) {
			Sender cur = (Sender) bean;
			lock.writeLock().lock();
			try {
				LOG.info("sender detected: " + beanName);
				senders.add(cur);
				Iterator<RoutedCollector> it = pendingInitialization.iterator();
				while (it.hasNext()) {
					RoutedCollector curCollector = it.next();
					if (curCollector.getDestination().equals(cur.getClass())) {
						LOG.info("pending collector initialized: " + curCollector.getClass());
						initCollector(cur, curCollector);
						it.remove();
						continue;
					}
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		if (bean instanceof Collector || bean instanceof RoutedCollector) {
			final Sender destination;
			if (bean instanceof RoutedCollector) {
				RoutedCollector curBean = (RoutedCollector) bean;
				lock.writeLock().lock();
				try {
					destination = getDestination(curBean.getDestination());
					if (destination == null) {
						LOG.info("collector: " + curBean.getDestination() + " not found. pending initialization: " + beanName);
						pendingInitialization.add(curBean);
					}
				} finally {
					lock.writeLock().unlock();
				}
			} else {
				destination = defaultSender;
			}
			final Collector cur = (Collector) bean;
			LOG.info("metric collector found: " + beanName);
			if (destination != null) {
				initCollector(destination, cur);
			}
		}
		return bean;
	}

	private void initCollector(final Sender destination, final Collector cur) {
		lock.writeLock().lock();
		try {
			if (executor == null) {
				LOG.info("starting processing thread");
				executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
					@Override
					public Thread newThread(Runnable task) {
						Thread thread = new Thread(task);
						thread.setName("collectd");
						thread.setDaemon(true);
						return thread;
					}
				});
			}
			ScheduledFuture<?> future = executor.scheduleAtFixedRate(new SafeRunnable() {
				@Override
				public void safeRun() {
					lock.writeLock().lock();
					try {
						Iterator<List<ValueList>> it = cur.iterator();
						while (it.hasNext()) {
							List<ValueList> data = it.next();
							if (LOG.isDebugEnabled()) {
								LOG.debug("processing metrics: " + data.size());
							}
							for (ValueList cur : data) {
								cur.setInterval(intervalSec);
							}
							if (!data.isEmpty()) {
								destination.dispatch(data);
								destination.flush();
							}
						}
					} finally {
						lock.writeLock().unlock();
					}
				}
			}, intervalSec, intervalSec, TimeUnit.SECONDS);
			collectors.put(cur, future);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		if (bean instanceof Collector) {
			lock.writeLock().lock();
			try {
				ScheduledFuture<?> future = collectors.remove(bean);
				if (future != null) {
					future.cancel(true);
				}
				if (collectors.isEmpty() && executor != null) {
					LOG.info("no collectors to process. stopping processing thread");
					executor.shutdownNow();
					executor = null;
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// do nothing
		return bean;
	}

	@PreDestroy
	public void stop() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}

	private Sender getDestination(Class<? extends Sender> cur) {
		for (Sender curDestination : senders) {
			if (curDestination.getClass().equals(cur)) {
				return curDestination;
			}
		}
		return null;
	}

	@Required
	public void setDefaultSender(Sender defaultSender) {
		this.defaultSender = defaultSender;
	}

	@Required
	public void setIntervalSec(long intervalSec) {
		this.intervalSec = intervalSec;
	}

}
