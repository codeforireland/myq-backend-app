package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;

import java.util.Collection;

public interface WaitingTimeEstimationStrategy {
	TicketEstimation estimateTimeToBeServiced(
            QueueDetails queueDetails, Collection<TicketUpdate> ticketUpdates, int ticketNumber);
}
