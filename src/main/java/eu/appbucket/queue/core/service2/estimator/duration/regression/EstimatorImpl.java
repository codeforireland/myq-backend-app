package eu.appbucket.queue.core.service2.estimator.duration.regression;

import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.log4j.Logger;

import java.util.Collection;

public class EstimatorImpl implements Estimator {

    private SimpleRegression regression;
    private int userTicketNumber;
    private Collection<Record> records;
    private static final Logger LOGGER = Logger.getLogger(EstimatorImpl.class);

    public EstimatorImpl(Collection<Record> records, int userTicketNumber) {
        this.records = records;
        this.userTicketNumber = userTicketNumber;
    }

    @Override
    public long estimateTimeAtWhichUserWillBeServed() {
        calculatedRegression();
        return estimateTime();
    }

    private long estimateTime() {
        double timeAtWhichUserWillBeServed = regression.predict(userTicketNumber);
        // logRegressionLineForDebuggingOnly(timeAtWhichUserWillBeServed);
        return (long) timeAtWhichUserWillBeServed;
    }

    private void calculatedRegression() {
        regression = new SimpleRegression(true);
        for(Record record: records) {
            if(record.isValid()) {
                regression.addData(record.getServedTicket(), record.getTime());
            }
        }
    }

    private void logRegressionLineForDebuggingOnly(double timeAtWhichUserWillBeServed) {
        StringBuilder output = new StringBuilder("\n");
        for(Record record: records) {
            output.append(record.getTime() + "\t" + record.getServedTicket() + "\n");
        }
        output.append(((long) timeAtWhichUserWillBeServed) + "\t" + userTicketNumber  + "\n");
        LOGGER.info(output.toString());
    }
}
