package com.aerse.bootstrap;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationBootstrapper {

	private static ClassPathXmlApplicationContext context;
	private final static Logger log = Logger.getLogger(ApplicationBootstrapper.class);

	public static void main(String[] args) {

		BootstrapUtil.init();

		if (args == null || args.length < 2) {
			log.warn("invalid arguments lenght. Expected: <bootstrapbean> <context1> ... <contextN>");
			return;
		}

		String[] contexts = new String[args.length - 1];
		StringBuilder builder = new StringBuilder();
		for (int i = 1, y = 0; i < args.length; i++, y++) {
			contexts[y] = args[i];
			builder.append(args[i]).append(" ");
		}
		log.info("loading bean: " + args[0] + " from contexts: " + builder.toString());
		long start = System.currentTimeMillis();
		context = new ClassPathXmlApplicationContext(contexts);
		context.registerShutdownHook();
		try {
			context.getBean(args[0]);
			log.info("==============================================");
			log.info("==== " + args[0] + " Started. Took: " + (System.currentTimeMillis() - start) + " ms ===");
			log.info("==============================================");
		} catch (Throwable e) {
			log.error("unable to start.", e);
			context.close();
			return;
		}
		if (context.containsBean("mbeanExporter")) {
			log.info("========= Starting JMX =========");
			try {
				context.getBean("mbeanExporter");
			} catch (Exception e) {
				log.error("Unable to start jmx", e);
			}
		}
	}

}
