package eu.appbucket.queue.core.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ManagementServiceImpl implements ManagementService {

	@CacheEvict(
			value = {"queuesCache", 
					"queueInfoCache", 
					"queueDetailsCache"}, 
			allEntries = true)
	public void flushStaticCache() {
	}
	
	
	@CacheEvict(
			value = {"queueStatsCache",
					"highestTicketUpdateCache"}, 
			allEntries = true)
	public void flushDynamicCache() {
	}
}
