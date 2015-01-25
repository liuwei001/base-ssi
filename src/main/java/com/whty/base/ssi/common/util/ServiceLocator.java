package com.whty.base.ssi.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 
 * Spring Bean工厂包装类
 * 
 * @author  t2w
 * @version  [V100R001, 2012-09-04]
 * @since  [SDP.BASE.SSI-V100R001]
 */
public class ServiceLocator implements BeanFactoryAware {
	protected static final Log logger = LogFactory.getLog(ServiceLocator.class);

	private static BeanFactory beanFactory = null;

	public void setBeanFactory(BeanFactory factory) throws BeansException {
		beanFactory = factory;
	}

	public static Object getBean(String beanName) {
		if (beanFactory != null) {
			return beanFactory.getBean(beanName);
		}
		return null;
	}

	public static Object getDao(String daoName) {
		return ServiceLocator.getBean(daoName);
	}

	public static Object getService(String serviceName) {
		return ServiceLocator.getBean(serviceName);
	}
}
