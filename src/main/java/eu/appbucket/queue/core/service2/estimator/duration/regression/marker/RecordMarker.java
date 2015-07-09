package eu.appbucket.queue.core.service2.estimator.duration.regression.marker;



import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;

import java.util.Collection;

public interface RecordMarker {

    void markRecords(Collection<Record> recordsToBeMarked);
    void setSuccessor(RecordMarker successor);
}
