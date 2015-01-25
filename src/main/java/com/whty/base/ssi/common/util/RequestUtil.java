package com.whty.base.ssi.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * 
 * HTTP请求的工具类 通过该工具类可以获取HTTP请求的IP地址，将普通的JavaBean转换成Map数据类型。
 * 
 * @author t2w
 * @version [V100R001, 2009-11-23]
 * @see LogInterceptor
 * @since [SDP.BASE.SSI-V100R001]
 */
public class RequestUtil {

	private static final RequestUtil instance = new RequestUtil();
	private static final Logger logger = Logger.getLogger(RequestUtil.class);

	/**
	 * 构造函数采用private域，该类不能进行实例化操作。
	 */
	private RequestUtil() {
	}

	public static RequestUtil getInstance() {
		return instance;
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @param request
	 *            - HTTP请求对象
	 * @return 返回String类型，用于保存获取到的IP地址。
	 * 
	 * @return String 返回获取的IP地址。
	 */
	public String getIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取服务器本地的IP地址，如果服务器端有多网卡，那么返回一个IP地址。
	 * 
	 * @return 返回服务器的IP地址
	 * 
	 * @return String 返回服务器的IP地址
	 */
	public String getLocalIPAddress() {
		Enumeration<NetworkInterface> netInterfaces = null; 
		String ip = null;
		try {  
		    netInterfaces = NetworkInterface.getNetworkInterfaces();  
		    while (netInterfaces.hasMoreElements()) {  
		        NetworkInterface ni = netInterfaces.nextElement();  
		        Enumeration<InetAddress> ips = ni.getInetAddresses();  
		        while (ips.hasMoreElements()) {  
		        	ip = ips.nextElement().getHostAddress();
		        	break;
		        }  
		    }  
		} catch (Exception e) {  
		   	ip = "127.0.0.1";
		}  
		
		return ip;
	}
	
	
	/**
	 * 从类路径中加载该类
	 * 根据传入的类名，从JVM类路径中加载该类型到内存中，并返回该类型对象
	 * @param className 全限定的类名
	 * @return 返回该类对象实例
	 * @throws ClassNotFoundException 从类型路径中无法找到该类
	 * 
	 * @return Class 返回该类对象实例
	 * @exception throws [非检测类型的异常] [从类型路径中无法找到该类]
	 */
	@SuppressWarnings( "unchecked" )
	public static Class applicationClass(String className)
			throws ClassNotFoundException {
		// Look up the class loader to be used
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			classLoader = RequestUtil.class.getClassLoader();
		}

		// Attempt to load the specified class
		return (classLoader.loadClass(className));
	}
	


}
