package eu.appbucket.queue.core.service2.estimator.duration.regression.marker;


import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Flag;
import eu.appbucket.queue.core.service2.estimator.duration.regression.record.Record;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DuplicatedRecordMarkerImpl implements RecordMarker {

    private Set<Record> recordsCache;
    private Record currentRecord;
    private RecordMarker nextRecordProcessor;

    public DuplicatedRecordMarkerImpl() {
        recordsCache = new HashSet<Record>();
    }

    @Override
    public void setSuccessor(RecordMarker successor) {
        nextRecordProcessor = successor;
    }

    @Override
    public void markRecords(Collection<Record> recordsToBeMarked) {
        Collection<Record> markedRecords = this.execute(recordsToBeMarked);
        if(nextRecordProcessor != null) {
            nextRecordProcessor.markRecords(markedRecords);
        }
    }

    protected Collection<Record> execute(Collection<Record> records) {
        for(Record record: records) {
            if(!record.isValid())
                continue;
            currentRecord = record;
            markCurrentRecord();
            storeCurrentRecord();
        }
        return records;
    }

    private void storeCurrentRecord() {
        recordsCache.add(currentRecord);
    }

    private void markCurrentRecord() {
        if(isDuplicate()) {
            currentRecord.setFlag(Flag.DUPLICATE_ENTRY);
        } else {
            currentRecord.setFlag(Flag.VALID);
        }
    }

    private boolean isDuplicate() {
        for(Record cachedRecords: recordsCache) {
            if(currentRecord.getUserTicket() == cachedRecords.getUserTicket() &&
                    currentRecord.getServedTicket() == cachedRecords.getServedTicket()) {
                return true;
            }
        }
        return false;
    }
}
