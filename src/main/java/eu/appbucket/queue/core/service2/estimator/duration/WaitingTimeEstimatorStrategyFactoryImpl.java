package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.service.estimator.duration.DefaultWaitingTimeEstimationStrategyImpl;
import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="v2.waitingTimeEstimatorStrategyFactory")
public class WaitingTimeEstimatorStrategyFactoryImpl implements WaitingTimeEstimatorStrategyFactory {

    private RegressionBasedEstimatorStrategyImpl regressionBasedEstimatorStrategy;
    private DefaultWaitingTimeEstimationStrategyWrapperImpl defaultWaitingTimeEstimationStrategyWrapper;
    private final static int MINIMUM_REQUIRED_UPDATES = 5;

    @Autowired
    public void setDefaultWaitingTimeEstimationStrategyWrapper(
            DefaultWaitingTimeEstimationStrategyWrapperImpl defaultWaitingTimeEstimationStrategyWrapper) {
        this.defaultWaitingTimeEstimationStrategyWrapper = defaultWaitingTimeEstimationStrategyWrapper;
    }

    @Autowired
    public void setRegressionBasedEstimatorStrategy(RegressionBasedEstimatorStrategyImpl regressionBasedEstimatorStrategy) {
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
