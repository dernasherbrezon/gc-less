package com.aerse.bootstrap;

class CurrentServer {

	private final String env;
	private final String lang;
	private final String serverID;

	public CurrentServer(String env, String lang, String serverID) {
		this.env = env;
		this.lang = lang;
		this.serverID = serverID;
	}

	public String getEnv() {
		return env;
	}

	public String getLang() {
		return lang;
	}

	public String getServerID() {
		return serverID;
	}

	@Override
	public String toString() {
		return "[env=" + env + ", lang=" + lang + ", serverID=" + serverID + "]";
	}

}
