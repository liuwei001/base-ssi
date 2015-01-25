package com.whty.base.ssi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import com.whty.common.config.SystemInitiator;


/**
 * 属性文件读取加载
 * 
 * @author t2w
 * @version [V100R001, 2012-09-04]
 * @since [SDP.BASE.SSI-V100R001]
 */

public class SSIPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {

	private Resource[] locations;

	private String fileEncoding;
	private boolean ignoreResourceNotFound = false;
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	protected void loadProperties(Properties props) throws IOException {
		String FS = System.getProperty("file.separator");

		if (this.locations != null) {
			for (int i = 0; i < this.locations.length; i++) {
				Resource location = this.locations[i];

				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				InputStream is = null;
				try {
					//String work_dir = System.getProperty("user.dir");
					String confPath = SystemInitiator.getSystemInfo().getConfPath();
					is = new FileInputStream(
							(confPath + File.separator + location.getFilename()));
					if (location.getFilename().endsWith(XML_FILE_EXTENSION)) {
						this.propertiesPersister.loadFromXml(props, is);
					} else {
						if (this.fileEncoding != null) {
							this.propertiesPersister
									.load(props, new InputStreamReader(is,
											this.fileEncoding));
						} else {
							this.propertiesPersister.load(props, is);
						}
					}
				} catch (IOException ex) {
					if (this.ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from "
									+ location + ": " + ex.getMessage());
						}
					} else {
						throw ex;
					}
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

}
