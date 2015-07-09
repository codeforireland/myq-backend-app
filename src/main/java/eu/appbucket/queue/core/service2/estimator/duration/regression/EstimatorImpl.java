package eu.appbucket.queue.core.service2.estimator.duration.regression;

import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Collection;

public class EstimatorImpl implements Estimator {

    private SimpleRegression regression;
    private int userTicketNumber;
    private Collection<Record> records;

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
        return (long) timeAtWhichUserWillBeServed;
    }

    private void calculatedRegression() {
        regression = new SimpleRegression(true);
        for(Record record: records) {
            regression.addData(record.getServedTicket(), record.getTimeStamp());
        }
    }
}
