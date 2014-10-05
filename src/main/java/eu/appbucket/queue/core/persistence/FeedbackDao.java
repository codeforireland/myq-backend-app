package eu.appbucket.queue.core.persistence;

import java.util.List;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter;

public interface FeedbackDao {
	void persistFeedbackRecord(FeedbackRecord record);
	List<FeedbackRecord> findFeedbackRecords(FeedbackRecordFilter filter);
}
