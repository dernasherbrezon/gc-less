package com.aerse.web;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class MessageSupport extends TagSupport {

	private static final long serialVersionUID = -8909175572648528401L;
	private String key;

	@Override
	public int doEndTag() throws JspException {
		ResourceBundle bundle = (ResourceBundle) pageContext.getAttribute("resourcebundle", PageContext.REQUEST_SCOPE);
		if (bundle == null) {
			outputKey();
		} else {
			String value = bundle.getString(key);
			if (value == null) {
				outputKey();
			} else {
				try {
					pageContext.getOut().append(value);
				} catch (Exception e) {
					throw new JspException(e);
				}
			}
		}
		return super.doEndTag();
	}

	private void outputKey() throws JspException {
		try {
			pageContext.getOut().append("????").append(key).append("????");
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
