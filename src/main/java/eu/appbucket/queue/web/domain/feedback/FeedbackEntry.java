package eu.appbucket.queue.web.domain.feedback;

import java.util.Date;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;


public class FeedbackEntry {
	
	private Integer queueId;
	private String comment;
	private int rating;
	private Date created;
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Integer getQueueId() {
		return queueId;
	}
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public static FeedbackEntry fromFeedbackRecord(FeedbackRecord record) {
		FeedbackEntry entry = new FeedbackEntry();
		entry.setQueueId(record.getQueueInfo().getQueueId());
		entry.setComment(record.getComment());
		entry.setRating(
				record.getRating() != null ? 
						record.getRating().getRatingValue() : 0);
		entry.setCreated(record.getCreated());
		return entry;
	}
}
