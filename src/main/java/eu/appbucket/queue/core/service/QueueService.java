package eu.appbucket.queue.core.service;

import java.util.Collection;
import java.util.Date;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.domain.queue.QueueStats;

public interface QueueService {
	
	Collection<QueueInfo> getProductionQueues();
	Collection<QueueInfo> getTestQueues();
	QueueInfo getQueueInfoByQueueId(int queueId);	                 	
	QueueDetails getQueueDetailsByQueueId(int queueId);
    Date getQueueOpeningTimeByQueueId(int queueId);
    Date getQueueClosingTimeByQueueId(int queueId);
	QueueStats getQueueStatsByQueueId(int queueId);
	void updateQueueStats(QueueStats queueStats); 
}
