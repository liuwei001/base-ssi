package com.whty.base.ssi.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.whty.base.ssi.common.Constants;

/**
 * 
 * 针对访问系统的URL进行权限的过滤，如果该用户发起的HTTP请求具备访问权限，
 * 那么用户的请求将继续进行，否则，权限过滤器将向客户端响应“您无权此操作”的提示信息。权限 过滤器可以通过web.xml进行配置，具体的配置如下：
 * 1、通过初始化参数名“doNotFilterURL”，配置不需要过滤的一些URL，并以“,”隔开。
 * 2、通过url-pattern配置需要权限过滤器过滤的url，当前的配置为“/*”，此配置可以根据实际情况 进行改动。
 * 
 * 该权限过滤器加入了缓存的功能，检测到用户第一次发起HTTP请求具备访问的权限，那么就将该请求
 * 缓存起来，当用户再次发起请求的时候，那么就从缓存中去判断该请求是否具有访问权限。
 * 
 * @author t2w
 * @version [V100R001, 2012-09-04]
 * @since [SDP.BASE.SSI-V100R001]
 */
public class PrivilegeFilter implements Filter {

	/** 日志记录 */
	private static Logger logger = Logger.getLogger(PrivilegeFilter.class);

	/** ajax请求 */
	private static final String AJAX_REQUEST_TYPE = "ajax";

	/** 缓存允许访问的URL */
	private static Map<String, Set<String>> privilegeURLCache = new HashMap<String, Set<String>>();

	/** 不需要过滤的URL */
	private String[] doNotFilterURL;

	public void init(FilterConfig filterConfig) throws ServletException {
		String params = filterConfig.getInitParameter("doNotFilterURL");
		if (params != null) {
			String urls[] = params.split(",");
			doNotFilterURL = new String[urls.length];
			for (int i = 0, size = urls.length; i < size; i++) {
				doNotFilterURL[i] = urls[i];
			}
		}
	}

	/**
	 * 对一些URL不进行过滤拦截。
	 * 
	 * 获取HTTP请求的URI，如果该请求是登录的首页，则不进行拦截。判断doNotFilterURL是否为null，
	 * 如果不为null，如果该数组中含HTTP请求的URI，则也不进行拦截。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param response
	 *            HTTP响应
	 * @param filterChain
	 *            过滤器链
	 * @return 返回 trur or false，来判断该HTTP请求的URL是否要进行拦截。
	 * @throws IOException
	 *             抛出HTTP请求的IO异常，由容器来捕获。
	 * @throws ServletException
	 *             抛出Servlet异常，由容器来捕获。
	 * 
	 * @return boolean - 返回true说明该HTTP请求的URL过滤器不进行拦截，否则过滤器对它进行拦截。
	 * @exception throws
	 *                IOException HTTP请求的IO异常，ServletException Servlet执行异常
	 */
	private boolean doNotFiler(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String requestPath = req.getRequestURI();
		String contextRoot = req.getContextPath();

		int length = contextRoot.length();
		String path = requestPath.substring(length);
		if (path != null && path.length() != 0) {
			path = path.trim();
		}

		/** 登录页面不进行拦截 */
		if (Constants.FIRST_LOGIN_URL.getStringValue().equals(path)) {
			return true;
		}

		boolean doNotFilter = false;
		if (doNotFilterURL != null) {
			for (String url : doNotFilterURL) {
				if (Constants.LOGIN_ACTION.getStringValue().equals(path)) {
					doNotFilter = true;
					break;
				}

				if (url != null && path.contains(url.trim())) {
					doNotFilter = true;
					break;
				}
			}
		}

		return doNotFilter;
	}

	/**
	 * 从缓存中判断该URL是否允许访问。
	 * 
	 * 根据用户的登录名，从缓存中取出该用户具备访问权限的所有URL，然后在判断这些所有的URL中是否
	 * 包含requestPath这个URL，如果包含则说明用户具备requestPath的访问权限，否则用户不具备该 requestPath的访问权限。
	 * 
	 * @param user
	 *            用户登录对象（含用户登录名、所属角色、拥有权限等信息），该对象是从session中读取。
	 * @param requestPath
	 *            客户端请求的URL。
	 * @return 返回true or false，来判断该登录用户具备访问权限的URL缓存中是否包含requestPath。
	 * 
	 * @return boolean 返回true说明登录用户具备访问权限的URL缓存中包含requestPath，该requestPath不在缓存中。
	 */
//	private boolean doFilterFromCache(Admin user, String requestPath) {
//		boolean canFilter = false;
//		if (privilegeURLCache.containsKey(user.getLoginName())) {
//			Set<String> urls = privilegeURLCache.get(user.getLoginName());
//			for (String url : urls) {
//				if (requestPath.equals(url)) {
//					canFilter = true;
//					break;
//				}
//			}
//		}
//
//		return canFilter;
//	}

	/**
	 * 将用户有权限访问的URL，放入缓存中。
	 * 
	 * 从缓存中读取该登录用户具备访问权限的所有URL列表，如果不存在这样的列表，则新建ArrayList对象，放入键=用户登录名的缓存中，
	 * 并将requetPath加入到URL列表中。
	 * 
	 * @param user
	 *            用户登录对象（含用户登录名、所属角色、拥有权限等信息），该对象是从session中读取。
	 * @param requestPath
	 *            客户端请求的URL。
	 */
//	private void addPrivilegeURL2Cache(Admin user, String requestPath) {
//		if (privilegeURLCache.containsKey(user.getLoginName())) {
//			Set<String> urls = privilegeURLCache.get(user.getLoginName());
//			if (urls == null) {
//				urls = new HashSet<String>();
//			}
//			synchronized (privilegeURLCache) {
//				urls.add(requestPath);
//			}
//		} else {
//			Set<String> urls = new HashSet<String>();
//			urls.add(requestPath);
//			synchronized (privilegeURLCache) {
//				privilegeURLCache.put(user.getLoginName(), urls);
//			}
//		}
//	}

	/**
	 * 访问的HTTP请求的URL中，去掉contextRoot。
	 * 
	 * 首先重HttpServletRequest对象中获取ContentRoot，然后再从该对象中获取http请求的URI，
	 * 最后从获取的URI字符串中去掉ContextRoot后，以字符串类型放回。
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回字符串类型
	 * 
	 * @return String 返回的字符串中去掉了ContentRoot。
	 */
	private String removeContextRoot(HttpServletRequest request) {
		String contextRoot = request.getContextPath();
		String requestPath = request.getRequestURI();
		if (requestPath.contains(contextRoot)) {
			requestPath = requestPath.substring(contextRoot.length());
		}

		return requestPath;
	}

	/**
	 * 对HTTP请求的URL进行拦截。
	 * 
	 * 1、先判断该请求是否可以不进行拦截，如果是直接执行过滤器链的下一个操作，返回。
	 * 2、判断从session中读取的登录用户对象是否为空，如果为空页面跳转到登录页面，返回。
	 * 3、从URL缓存中判断是否存在该登录用户的，这个请求的URL，如果是true，则执行过滤器链的下一个操作。
	 * 4、从session中获取该用户的所有权限列表，从这个权限列表中判断是否存在该请求的URL。如果存在，那么
	 * 执行过滤器链的下一个操作，否则，页面跳转到“您无权此操作”的提示。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param response
	 *            HTTP响应
	 * @param filterChain
	 *            过滤器链
	 * @throws IOException
	 *             抛出HTTP请求的IO异常，由容器来捕获。
	 * @throws ServletException
	 *             抛出Servlet异常，由容器来捕获。
	 * 
	 * @exception throws
	 *                IOException HTTP请求的IO异常，ServletException Servlet执行异常
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
//
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//		HttpSession session = req.getSession();
//		Admin user = (Admin) session.getAttribute("loginUser");
//
//		// 将缓存放入session中
//		Map<String, Set<String>> cache = (Map<String, Set<String>>) session
//				.getAttribute("privilegeURLCache");
//		if (cache == null) {
//			session.setAttribute("privilegeURLCache", privilegeURLCache);
//		}
//
//		// 获取HTTP请求的URI
//		String requestPath = removeContextRoot(req);
//
//		// 不进行拦截
//		boolean doNotFilter = doNotFiler(request, response, filterChain);
//		if (doNotFilter) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		// 用户未登录，或者登录的用户session失效，请求将转发到登录页面。
//		if (user == null) {
//			logger
//					.warn("the session is invalide, so privilege filter forward the request to the login again!");
//			req.getRequestDispatcher(Constants.LOGOUT_URL.getStringValue())
//					.forward(req, res);
//
//			return;
//		}
//
//		boolean hasPrivilege = false;
//		// 从缓存中判断该URL是否允许访问
//		hasPrivilege = doFilterFromCache(user, requestPath);
//		if (!hasPrivilege) {
//			// 从session获取用户所拥有的权限列表
//			List<Privilege> privileges = (List<Privilege>) session
//					.getAttribute("assignedPrivileges");
//
//			for (Privilege privilege : privileges) {
//				String privilegeURL = privilege.getPrivilegeURL();
//
//				// 空串不需要解析
//				if (privilegeURL == null || privilegeURL.length() == 0) {
//					continue;
//				}
//				int index = -1;
//				// 解析多个字符串，如url1, url2, url3...
//				String[] urls = privilegeURL.split(",");
//				for (String url : urls) {
//					url = url.trim();
//					// 过滤掉查询串
//					if (url != null && url.length() != 0) {
//						index = privilegeURL.indexOf("?");
//						if (index != -1) {
//							url = url.substring(0, index);
//						}
//					}
//
//					// 过滤掉查询串以后，再和http请求的URL进行比较
//					if (requestPath.equals(url)) {
//						hasPrivilege = true;
//						// 加入缓存
//						addPrivilegeURL2Cache(user, requestPath);
//						break;
//					}
//				}
//
//				if (hasPrivilege) {
//					break;
//				}
//			}
//		}
//
//		// 如果该URL在权限列表中不存在，那么跳转到无权限的提示页面。
//		if (!hasPrivilege) {
//			// 处理HTTP请求，是否有ajax请求
//			handleRequestTypeBy(req, res);
//			logger.warn("you don't have the privilege to run the url of '"
//					+ requestPath + "'.");
//
//			return;
//		} else {
//			filterChain.doFilter(request, response);
//		}
	}

	/**
	 * 处理请求类型。 获取参数名为requestType的值，如果值为ajax，则向客户端放回一个json格式的对象。 否而，跳转到无权操作的提示页面。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param response
	 *            HTTP响应
	 * @throws IOException
	 *             HTTP请求的IO异常
	 * @exception throws
	 *                IOException HTTP请求的IO异常
	 */
	private void handleRequestTypeBy(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String requestType = request.getParameter("requestType");
		if (AJAX_REQUEST_TYPE.equals(requestType)) {
			response.setContentType("application/text;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("hasNoPrivilege");
			out.flush();
			out.close();
		} else {
			String contextRoot = request.getContextPath();
			String noPrivilegeOnlyText = request
					.getParameter("noPrivilegeOnlyText");
			if (noPrivilegeOnlyText != null) {
				response
						.sendRedirect(contextRoot
								+ Constants.NO_PRIVILEGE_URL_ONLY_TEXT
										.getStringValue());
			} else {
				response.sendRedirect(contextRoot
						+ Constants.NO_PRIVILEGE_URL.getStringValue());
			}
		}
	}

	/**
	 * 清理操作
	 */
	public void destroy() {
		this.doNotFilterURL = null;
	}
}
