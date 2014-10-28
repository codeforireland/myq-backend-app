package eu.appbucket.queue.core.service.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.appbucket.queue.core.service.ManagementService;

@EnableScheduling
@Service
public class DynamicCacheFlushScheduler {
	
	private ManagementService managementService;
	private static final Logger LOGGER = Logger.getLogger(DynamicCacheFlushScheduler.class);
			
	@Autowired
	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}
	
	@Scheduled(cron="0 0 * * * *")
	public void flushAtMidnignDynamicCache() {
		LOGGER.info("flushAtMidnignDynamicCache - Flushing dynamic cache at midnight.");
		managementService.flushDynamicCache();
	}
}
