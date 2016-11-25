package com.aerse.mvc; // NOPMD by dernasherbrezon on 16.10.11 9:56

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class ProxyedHTTPServletRequest implements HttpServletRequest {

	private final HttpServletRequest impl;
	private final String frontEnd;
	
	public ProxyedHTTPServletRequest(HttpServletRequest impl, String frontEnd) {
		this.impl = impl;
		this.frontEnd = frontEnd;
	}

	@Override
	public Object getAttribute(String name) {
		return impl.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return impl.getAttributeNames();
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return impl.authenticate(response);
	}
	
	@Override
	public String getCharacterEncoding() {
		return impl.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		impl.setCharacterEncoding(env);
	}

	@Override
	public int getContentLength() {
		return impl.getContentLength();
	}

	@Override
	public String getContentType() {
		return impl.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return impl.getInputStream();
	}

	@Override
	public String getParameter(String name) {
		return impl.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return impl.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return impl.getParameterValues(name);
	}

	@Override
	public Map<String,String[]> getParameterMap() {
		return impl.getParameterMap();
	}

	@Override
	public String getProtocol() {
		return impl.getProtocol();
	}

	@Override
	public String getScheme() {
		return impl.getScheme();
	}

	@Override
	public String getServerName() {
		return impl.getServerName();
	}

	@Override
	public int getServerPort() {
		return impl.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return impl.getReader();
	}

	@Override
	public String getRemoteAddr() {
		return impl.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return impl.getRemoteHost();
	}

	@Override
	public void setAttribute(String name, Object o) {
		impl.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		impl.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return impl.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return impl.getLocales();
	}

	@Override
	public boolean isSecure() {
		return impl.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return impl.getRequestDispatcher(path);
	}

	@Override
	@Deprecated
	public String getRealPath(String path) {
		return impl.getRealPath(path);
	}

	@Override
	public int getRemotePort() {
		return impl.getRemotePort();
	}

	@Override
	public String getLocalName() {
		return impl.getLocalName();
	}

	@Override
	public String getLocalAddr() {
		return impl.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return impl.getLocalPort();
	}

	@Override
	public String getAuthType() {
		return impl.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return impl.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return impl.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return impl.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return impl.getHeaders(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return impl.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return impl.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return impl.getMethod();
	}

	@Override
	public String getPathInfo() {
		return impl.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return impl.getPathTranslated();
	}

	@Override
	public String getContextPath() {
		return impl.getContextPath();
	}

	@Override
	public String getQueryString() {
		return impl.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return impl.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String role) {
		return impl.isUserInRole(role);
	}

	@Override
	public Principal getUserPrincipal() {
		return impl.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return impl.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return impl.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer result = new StringBuffer(frontEnd);
		result.append(getRequestURI());
		return result;
	}

	@Override
	public String getServletPath() {
		return impl.getServletPath();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return impl.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return impl.getSession();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return impl.isRequestedSessionIdValid();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return impl.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return impl.isRequestedSessionIdFromURL();
	}

	@Override
	@Deprecated
	public boolean isRequestedSessionIdFromUrl() {
		return impl.isRequestedSessionIdFromUrl();
	}

	@Override
	public ServletContext getServletContext() {
		return impl.getServletContext();
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return impl.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
		return impl.startAsync(servletRequest, servletResponse);
	}

	@Override
	public boolean isAsyncStarted() {
		return impl.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return impl.isAsyncSupported();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return impl.getAsyncContext();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return impl.getDispatcherType();
	}

	@Override
	public void login(String username, String password) throws ServletException {
		impl.login(username, password);
	}

	@Override
	public void logout() throws ServletException {
		impl.logout();
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return impl.getParts();
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return impl.getPart(name);
	}
	
	
}
