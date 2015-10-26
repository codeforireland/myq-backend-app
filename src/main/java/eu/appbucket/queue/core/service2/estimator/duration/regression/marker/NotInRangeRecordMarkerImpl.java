package eu.appbucket.queue.core.service2.estimator.duration.regression.marker;

import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Flag;
import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * Marks record as Flag.NOT_IN_RANGE if the record does not contain expected value.
 *
 * Definition:
 * - median: http://www.mathsisfun.com/median.html
 * and "How do I know which measure of central tendency to use?" http://www.regentsprep.org/regents/math/algebra/ad2/measure.htm
 */
public class NotInRangeRecordMarkerImpl implements RecordMarker {

    private final static double RANGE_MARGIN_150_PERCENT = 1.5d;
    private Record currentRecord;
    private RecordMarker nextRecordProcessor;
    private long timeBase;
    private Collection<Record> recordsToBeMarked;
    double timeToServeTicketMedian;
    private static final Logger LOGGER = Logger.getLogger(NotInRangeRecordMarkerImpl.class);

    public NotInRangeRecordMarkerImpl(long timeBase) {
        this.timeBase=timeBase;
    }

    @Override
    public void setSuccessor(RecordMarker successor) {
        nextRecordProcessor = successor;
    }


    @Override
    public void markRecords(Collection<Record> recordsToBeMarked) {
        this.recordsToBeMarked = recordsToBeMarked;
        calculateTicketTimeToServeMedian();
        Collection<Record> markedRecords = this.execute(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.markRecords(markedRecords);
        }
    }

    private void calculateTicketTimeToServeMedian() {
        Median median = new Median();
        double [] deltas = new double[recordsToBeMarked.size()];
        int i = 0;
        double timeDurationFromTimeBaseToRecordTime;
        for(Record record: recordsToBeMarked) {
            timeDurationFromTimeBaseToRecordTime = record.getTime() - timeBase;
            deltas[i++] = timeDurationFromTimeBaseToRecordTime / record.getServedTicket();
        }
        timeToServeTicketMedian = median.evaluate(deltas);
    }

    private Collection<Record> execute(Collection<Record> records) {
        for(Record record: records) {
            if(!record.isValid())
                continue;
            currentRecord = record;
            markCurrentRecord();
        }
        return records;
    }

    private void markCurrentRecord() {
        if(isRecordValueOutOfRange()) {
            currentRecord.setFlag(Flag.NOT_IN_RANGE);
        } else {
            currentRecord.setFlag(Flag.VALID);
        }
    }

    /**
     * Checks if record value is in the margin of expected values.
     *
     * For example if we have following records:
     * r1: timeStamp = 100, ticket = 1
     * r2: timeStamp = 200, ticket = 2
     * then the average median of serving one ticket is 100 (delta)
     * so the margin minimum is:
     * min = delta / RANGE_MARGIN_150_PERCENT = 33.33
     * and margin maximum is:
     * max - delta * RANGE_MARGIN_150_PERCENT = 150
     * if we want to test record
     * r3: timeStamp = 300, ticket = 3
     * then it will be marked as valid because:
     * 300 / 3 -> 100 and is true: min(33.33) < 100 < max(150)
     * and if we want to test record
     * r4: timeStamp = 500, ticket = 10
     * then it will be still marked as valid because:
     * 500 / 10 -> 50 and is true: min(33.33) < 50 < max(150)
     * but if we want to test record
     * r5: timeStamp = 1000, ticket = 100
     * then it will be marked as invalid because:
     * 1000 / 100 = 10 but is false: min(33.33) < 10 < max(150)
     * the record r5 is out of the expected range
     *
     * @return true if record is in expected range and false is out of the expected range
     */
    private boolean isRecordValueOutOfRange() {
        double maxCutOffDelta = timeToServeTicketMedian * RANGE_MARGIN_150_PERCENT;
        double minCutOffDelta = timeToServeTicketMedian / RANGE_MARGIN_150_PERCENT;
        double deltaDurationToServeTicket = Math.abs(currentRecord.getTime() - timeBase) / currentRecord.getServedTicket();
        if(deltaDurationToServeTicket > maxCutOffDelta ||
                deltaDurationToServeTicket < minCutOffDelta) {
            return true;
        }
        return false;
    }

    /*private void logRecordsForDebuggingOnlyCollection(Collection<Record> recordsMarked) {
        for (Record record : recordsMarked) {
            LOGGER.debug("Not in range record marker: " + record.getTime() + "\t" + record.getFlag() + "\n");
        }
    }*/
}
