package eu.appbucket.queue.core.service2;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.domain.queue.QueueStats;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;

public interface TicketService {
	
	TicketEstimation getTicketEstimation(QueueInfo queueInfo, QueueDetails queueDetails, int ticketId);
	void persistTicketUpdate(TicketUpdate ticketUpdate);
	
}
