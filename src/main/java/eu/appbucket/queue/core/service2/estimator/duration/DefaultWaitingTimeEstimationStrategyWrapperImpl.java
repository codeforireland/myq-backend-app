package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueStats;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;
import eu.appbucket.queue.core.service.estimator.duration.DefaultWaitingTimeEstimationStrategyImpl;
import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This default strategy calculated waiting time based on the client ticket number, current time 
 * and default average service duration in the queue.
 * This strategy is used when there is not enough queue updates available to make correct
 * estimation.
 */
@Component("defaultWaitingTimeEstimationStrategyWrapper")
public class DefaultWaitingTimeEstimationStrategyWrapperImpl implements WaitingTimeEstimationStrategy {

    private eu.appbucket.queue.core.service.estimator.duration.WaitingTimeEstimationStrategy defaultWaitingTimeEstimationStrategy;

    @Autowired
    public void setDefaultWaitingTimeEstimationStrategy(
            DefaultWaitingTimeEstimationStrategyImpl defaultWaitingTimeEstimationStrategy) {
        this.defaultWaitingTimeEstimationStrategy = defaultWaitingTimeEstimationStrategy;
    }

    public TicketEstimation estimateTimeToBeServiced(QueueDetails queueDetails, Collection<TicketUpdate> ticketUpdates, int ticketNumber) {
        return defaultWaitingTimeEstimationStrategy.estimateTimeToBeServiced(queueDetails, null, ticketNumber);
    }
}
