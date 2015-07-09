package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimationStrategy;

public interface WaitingTimeEstimatorStrategyFactory {

    WaitingTimeEstimationStrategy getStrategy(int numberOfTicketUpdates);
}
