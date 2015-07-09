package eu.appbucket.queue.core.service2.estimator.duration.regression.marker;

import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Flag;
import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.Collection;

public class NotInRangeRecordMarkerImpl implements RecordMarker {

    private final static double RANGE_MARGIN_150_PERCENT = 1.5d;
    private Record currentRecord;
    private RecordMarker nextRecordProcessor;
    private long timeBase;
    private Collection<Record> recordsToBeMarked;
    double timeToServedTicketMedian;

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
        calculateTimeToServedTicketMedian();
        Collection<Record> markedRecords = this.execute(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.markRecords(markedRecords);
        }
    }

    private void calculateTimeToServedTicketMedian() {
        Median median = new Median();
        double [] deltas = new double[recordsToBeMarked.size()];
        int i = 0;
        double timeDurationFromTimeBaseToRecordTimestamp;
        for(Record record: recordsToBeMarked) {
            timeDurationFromTimeBaseToRecordTimestamp = record.getTimeStamp() - timeBase;
            deltas[i++] = timeDurationFromTimeBaseToRecordTimestamp / record.getServedTicket();
        }
        timeToServedTicketMedian = median.evaluate(deltas);
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

    private boolean isRecordValueOutOfRange() {
        double maxCutOffDelta = timeToServedTicketMedian * RANGE_MARGIN_150_PERCENT;
        double minCutOffDelta = timeToServedTicketMedian / RANGE_MARGIN_150_PERCENT;
        double deltaDurationToServedTicket = Math.abs(currentRecord.getTimeStamp() - timeBase) / currentRecord.getServedTicket();
        if(deltaDurationToServedTicket > maxCutOffDelta ||
                deltaDurationToServedTicket < minCutOffDelta) {
            return true;
        }
        return false;
    }
}
