package com.yyxnb.java.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https 域名校验
 *
 * @author yyx
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// 直接返回true
		return true;
	}
}
