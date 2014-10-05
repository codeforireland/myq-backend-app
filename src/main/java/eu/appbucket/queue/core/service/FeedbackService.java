package eu.appbucket.queue.core.service;

import java.util.List;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter;

public interface FeedbackService {
	
	void storeFeedack(FeedbackRecord feedbackRecord);
	List<FeedbackRecord> findFeedbacks(FeedbackRecordFilter filter);
}
