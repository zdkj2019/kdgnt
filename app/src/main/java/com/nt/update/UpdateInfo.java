package com.nt.update;

public class UpdateInfo {
	private String version;
	private String url;
	private String description;
	private static String content;
	private static String serverVersion;
	private static String localVersion;

	public static String getServerVersion() {
		return serverVersion;
	}

	public static void setServerVersion(String serverVersion) {
		UpdateInfo.serverVersion = serverVersion;
	}

	public static String getLocalVersion() {
		return localVersion;
	}

	public static void setLocalVersion(String localVersion) {
		UpdateInfo.localVersion = localVersion;
	}

	public static String getContent() {
		return content;
	}

	public static void setContent(String content) {
		UpdateInfo.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
