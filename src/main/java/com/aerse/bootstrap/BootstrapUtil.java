package com.aerse.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.xml.PropertiesDOMConfigurator;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.aerse.gcless.StringUtils;

final public class BootstrapUtil {

	private final static Logger log = Logger.getLogger(BootstrapUtil.class);
	private final static Pattern HOSTNAME = Pattern.compile("^(prod|dev|uat)-(en|ru)-(\\d+)$");

	public static void init() {
		String localhostname;
		try {
			localhostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			log.error("unable to resolve localhost", e1);
			return;
		}
		
		System.setProperty("localhostname", localhostname);

		CurrentServer serverConfigFromHost = null;

		Matcher m = HOSTNAME.matcher(localhostname);
		if (m.find()) {
			serverConfigFromHost = new CurrentServer(m.group(1), m.group(2), m.group(3));
		}

		String lang;
		if (System.getProperty("lang") != null) {
			lang = System.getProperty("lang");
		} else {
			// JVM standard parameter
			lang = System.getProperty("user.language");
		}
		CurrentServer serverConfigFromParams = new CurrentServer(System.getProperty("env"), lang, System.getProperty("serverID"));

		String env = null;
		if (serverConfigFromHost != null) {
			env = reinitProperty("env", serverConfigFromParams.getEnv(), serverConfigFromHost.getEnv());
			lang = reinitProperty("lang", serverConfigFromParams.getLang(), serverConfigFromHost.getLang());
			reinitProperty("serverID", serverConfigFromParams.getServerID(), serverConfigFromHost.getServerID());
		}

		String secretConfig = System.getProperty("secret.configuration");
		if (StringUtils.isBlank(secretConfig)) {
			secretConfig = "secret.properties";
		}
		InputStream secretProperties = null;
		try {
			secretProperties = ApplicationBootstrapper.class.getClassLoader().getResourceAsStream(secretConfig);
			if (secretProperties != null) {
				System.out.println("loading secret file: " + secretConfig);
				Properties props = new Properties();
				props.load(secretProperties);
				String smtpPassword = props.getProperty("SMTPPassword");
				if (smtpPassword != null) {
					System.setProperty("SMTPPassword", smtpPassword);
				}
			}
		} catch (Exception e) {
			log.error("unable to load SMTPPassword", e);
		} finally {
			if (secretProperties != null) {
				try {
					secretProperties.close();
				} catch (IOException e) {
					log.error("unable to close cursor", e);
				}
			}
		}

		// init log4j
		String log4jConfigurationPath = System.getProperty("log4j.configuration");
		if (log4jConfigurationPath != null) {
			URL log4jFile = ApplicationBootstrapper.class.getClassLoader().getResource(log4jConfigurationPath);
			if (log4jFile != null) {
				PropertiesDOMConfigurator configurator = PropertiesDOMConfigurator.configure(System.getProperties(), log4jFile);
				Appender core = configurator.findByName("asyncworkshop");
				if (core != null && (core instanceof AppenderAttachable) && env != null && env.equals("dev")) {
					AppenderAttachable coreAppender = (AppenderAttachable) core;
					PatternLayout patternLayout = new PatternLayout();
					patternLayout.setConversionPattern("%d{dd MMM yyyy HH:mm:ss,SSS} %p %t %c - %m%n");
					ConsoleAppender consoleAppender = new ConsoleAppender(patternLayout, "System.out");
					coreAppender.addAppender(consoleAppender);
				}

				LogManager.getLogManager().reset();
				SLF4JBridgeHandler.install();
				java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);
			}
		}

		if (serverConfigFromHost != null) {
			log.info("detected environment: " + serverConfigFromHost.toString());
		} else {
			log.warn("invalid hostname format: " + localhostname + " expected: " + HOSTNAME.pattern() + ". fallback to command line parameters");
		}

		log.info("environment command-line overrides: " + serverConfigFromParams.toString());

		Locale hostLocale = getLocaleByLanguage(lang);
		if (hostLocale == null) {
			log.warn("unable to detect locale by host language: " + lang + ". fallback to platform default: " + Locale.getDefault());
		} else {
			Locale.setDefault(hostLocale);
		}
	}

	private static String reinitProperty(String name, String firstPriority, String secondPriority) {
		if (firstPriority == null) {
			System.setProperty(name, secondPriority);
			return secondPriority;
		} else {
			System.setProperty(name, firstPriority);
			return firstPriority;
		}
	}

	private static Locale getLocaleByLanguage(String lang) {
		if (lang == null) {
			return null;
		}
		String country = System.getProperty("user.country");
		for (Locale cur : Locale.getAvailableLocales()) {
			if (cur.getLanguage().equals(lang) && (country == null || cur.getCountry().equals(country))) {
				return cur;
			}
		}
		return null;
	}

	private BootstrapUtil() {
		// do nothing
	}
}
