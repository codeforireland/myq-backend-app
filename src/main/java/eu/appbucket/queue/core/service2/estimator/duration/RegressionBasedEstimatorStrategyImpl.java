package eu.appbucket.queue.core.service2.estimator.duration;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;
import eu.appbucket.queue.core.service2.estimator.duration.regression.Estimator;
import eu.appbucket.queue.core.service2.estimator.duration.regression.EstimatorImpl;
import eu.appbucket.queue.core.service2.estimator.duration.regression.marker.DuplicatedRecordMarkerImpl;
import eu.appbucket.queue.core.service2.estimator.duration.regression.marker.NotInRangeRecordMarkerImpl;
import eu.appbucket.queue.core.service2.estimator.duration.regression.marker.RecordMarker;
import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Flag;
import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class RegressionBasedEstimatorStrategyImpl implements WaitingTimeEstimationStrategy {

    private static final Logger LOGGER = Logger.getLogger(RegressionBasedEstimatorStrategyImpl.class);

    public TicketEstimation estimateTimeToBeServiced(
            QueueDetails queueDetails, Collection<TicketUpdate> ticketUpdates, int ticketNumber) {
        long openingTimeToday = queueDetails.getTodayOpeningTimesUTC().getOpeningTime();
        Collection<Record> records = ticketUpdatesToRecords(ticketUpdates);
        newRecordMarker(openingTimeToday).markRecords(records);
        logOnlyValidRecords(records);
        TicketEstimation ticketEstimation = new TicketEstimation();
        ticketEstimation.setTimeToBeServiced(estimateTimeAtWhichUserWillBeServed(records, ticketNumber));
        return ticketEstimation;
    }

    private void logOnlyValidRecords(Collection<Record> records) {
        for(Record currentRecord: records) {
            if(currentRecord.getFlag() == Flag.VALID) {
                LOGGER.info("Record: " + currentRecord.getId() + " was marked as VALID");
            }
        }
    }

    protected RecordMarker newRecordMarker(long openingTimeToday) {
        DuplicatedRecordMarkerImpl duplicatedRecordMarker = new DuplicatedRecordMarkerImpl();
        NotInRangeRecordMarkerImpl notInRangeRecordMarker = new NotInRangeRecordMarkerImpl(openingTimeToday);
        duplicatedRecordMarker.setSuccessor(notInRangeRecordMarker);
        RecordMarker recordMarker = duplicatedRecordMarker;
        return recordMarker;
    }

    private Collection<Record> ticketUpdatesToRecords(Collection<TicketUpdate> ticketUpdates) {
        Collection<Record> records = new HashSet<Record>();
        for(TicketUpdate ticketUpdate: ticketUpdates) {
            records.add(Record.fromTicketUpdate(ticketUpdate));
        }
        return records;
    }

    private long estimateTimeAtWhichUserWillBeServed(Collection<Record> records, int userTicketNumber) {
        return newRegressionEstimator(records, userTicketNumber).estimateTimeAtWhichUserWillBeServed();
    }

    protected Estimator newRegressionEstimator(Collection<Record> records, int userTicketNumber) {
        return new EstimatorImpl(records, userTicketNumber);
    }
}
