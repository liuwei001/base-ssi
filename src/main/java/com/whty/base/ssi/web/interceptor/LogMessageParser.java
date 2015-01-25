package com.whty.base.ssi.web.interceptor;

import java.util.Date;

import org.apache.log4j.Logger;

import com.whty.base.ssi.common.businesslog.entity.BusinessLog;


/**
 * 
 * 操作码信息的解析器
 * 解析在LogDefinitions.properties中定义的操作码信息，封装成LogMessageBean对象
 * 
 * @author  t2w
 * @version  [V100R001, 2009-11-23]
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class LogMessageParser {
	
	private static final Logger logger = Logger.getLogger( LogMessageParser.class);
	
	/**
	 * 将操作信息解析成LogMessageBean对象。
	 * 
	 * 根据输入的操作信息，解析成LogMessageBean对象。
	 * @param messageContent - 具体的操作信息，操作信息记录在LogDefinitions.properties文件中。
	 * @return 返回LogMessageBean对象
	 * 
	 * @return LogMessageBean 返回LogMessageBean对象，封装具体的操作信息
	 */
	public static LogMessageBean convert2LogMessageBean( String messageContent ) {
		if ( messageContent == null ) {
			return null;
		}
		int index = messageContent.indexOf( "," );
		if ( index == -1 ) {
			return null;
		}
		
		LogMessageBean messageBean = new LogMessageBean();
		String [] temps = messageContent.split( "," );
		if ( temps == null || temps.length < 3 ) {
			return null;
		}
		
		messageBean.setOperateType( temps[0].trim() );
		messageBean.setOperateModule( temps[1].trim() );
		messageBean.setOperateObject( temps[2] );
		
		return messageBean;
	}
	
	/**
	 * 创建日志实体，日志实体的信息来源与日志配置文件。
	 * 根据操作码，从LogDefinitions.properties读取操作信息，并将信息封装成LogMessageBean对象。
	 * @param userId - 登录用户的ID号
	 * @param ipAddr - 客户端请求的IP地址
	 * @param operateCode - 操作代码
	 * @param messageContent - 错误代码所对应的错误信息
	 * @param operateDesc - 操作描述
	 * @return 日志实体
	 * 
	 * @return BusinessLog 返回IEPG后台定义的日志实体。
	 */
	public static BusinessLog createLog( int userId, String ipAddr, String operateCode, String messageContent, String operateDesc ) {
		LogMessageBean messageBean = LogMessageParser.convert2LogMessageBean( messageContent );
		if ( messageBean != null ) {
			BusinessLog log = new BusinessLog();
			log.setOperateType( messageBean.getOperateType() );
			log.setOperatModuleName( messageBean.getOperateModule() );
			log.setOperateObject( messageBean.getOperateObject() );
			log.setUserId( userId );
			log.setIpAddr(ipAddr);
			log.setOperateTime( new Date() );
			log.setDescription( operateDesc );
			
			return log;
		} else {
			logger.warn( "there is no '"+operateCode+"' define in LogDefinitions.xml" );
			return null;
		}
	}
}