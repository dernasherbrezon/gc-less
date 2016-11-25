package org.apache.log4j.xml;

import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;

public class PropertiesDOMConfigurator extends DOMConfigurator {

	static public PropertiesDOMConfigurator configure(Properties props, URL url) throws FactoryConfigurationError {
		PropertiesDOMConfigurator result = new PropertiesDOMConfigurator();
		result.doConfigure(props, url, LogManager.getLoggerRepository());
		return result;
	}

	public void doConfigure(Properties props, URL url, LoggerRepository repository) {
		this.props = props;
		super.doConfigure(url, repository);
	}

	public Appender findByName(String name) {
		return (Appender) appenderBag.get(name);
	}

}
