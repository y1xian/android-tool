package com.yyxnb.java.http.ssl;

import com.yyxnb.java.io.IORuntimeException;

/**
 * 兼容android低版本SSL连接<br>
 * 在测试HttpUrlConnection的时候，发现一部分手机无法连接[GithubPage]
 *
 * <p>
 * 最后发现原来是某些SSL协议没有开启
 *
 * @author yyx
 */
public class AndroidSupportSSLFactory extends CustomProtocolsSSLFactory {

	// Android低版本不重置的话某些SSL访问就会失败
	private static final String[] PROTOCOLS = {
			SSLProtocols.SSLv3, SSLProtocols.TLSv1, SSLProtocols.TLSv11, SSLProtocols.TLSv12};

	public AndroidSupportSSLFactory() throws IORuntimeException {
		super(PROTOCOLS);
	}

}