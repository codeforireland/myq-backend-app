package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.service.estimator.duration.DefaultWaitingTimeEstimationStrategyImpl;
import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimationStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public class WaitingTimeEstimatorStrategyFactoryImpl implements WaitingTimeEstimatorStrategyFactory {

    private WaitingTimeEstimationStrategy regressionBasedEstimatorStrategy;
    private WaitingTimeEstimationStrategy defaultWaitingTimeEstimationStrategyWrapper;
    private final static int MINIMUM_REQUIRED_UPDATES = 5;

    @Autowired
    public void setDefaultWaitingTimeEstimationStrategyWrapper(
            WaitingTimeEstimationStrategy defaultWaitingTimeEstimationStrategyWrapper) {
        this.defaultWaitingTimeEstimationStrategyWrapper = defaultWaitingTimeEstimationStrategyWrapper;
    }

    @Autowired
    public void setRegressionBasedEstimatorStrategy(WaitingTimeEstimationStrategy regressionBasedEstimatorStrategy) {
        this.regressionBasedEstimatorStrategy = regressionBasedEstimatorStrategy;
    }

    public WaitingTimeEstimationStrategy getStrategy(int numberOfTicketUpdates) {
        if(numberOfTicketUpdates < MINIMUM_REQUIRED_UPDATES) {
            return defaultWaitingTimeEstimationStrategyWrapper;
        } else {
            return regressionBasedEstimatorStrategy;
        }
    }
}
