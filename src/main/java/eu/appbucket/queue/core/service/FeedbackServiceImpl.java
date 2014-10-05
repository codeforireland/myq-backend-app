package eu.appbucket.queue.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter;
import eu.appbucket.queue.core.persistence.FeedbackDao;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	private FeedbackDao feedbackDao;
	
	@Autowired
	public void setFeedbackDao(FeedbackDao feedbackDao) {
		this.feedbackDao = feedbackDao;
	}
	
	public void storeFeedack(FeedbackRecord feedbackRecord) {
		feedbackDao.persistFeedbackRecord(feedbackRecord);
	}

	public List<FeedbackRecord> findFeedbacks(FeedbackRecordFilter filter) {
		return feedbackDao.findFeedbackRecords(filter);		
	}
}
