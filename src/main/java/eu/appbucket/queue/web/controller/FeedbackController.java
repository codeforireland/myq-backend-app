package eu.appbucket.queue.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter.FeedbackRecordFilterBuilder;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.service.FeedbackService;
import eu.appbucket.queue.core.service.QueueService;
import eu.appbucket.queue.web.domain.feedback.FeedbackEntry;

@Controller
public class FeedbackController {
	
	private static final Logger LOGGER = Logger.getLogger(FeedbackController.class);
	private FeedbackService feedbackService;
	private QueueService queueService;
	
	@Autowired
	public void setQueueService(QueueService queueService) {
		this.queueService = queueService;
	}
	
	@Autowired
	public void setFeedbackService(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}
	
	@RequestMapping(value = "feedbacks", method = RequestMethod.POST)
	@ResponseBody
	public void postFeedback(@RequestBody FeedbackEntry feedbackEntry) {
		QueueInfo queueInfo = getQueueInfoFromFeedback(feedbackEntry);
		LOGGER.info("postFeedback - queueInfo: " + queueInfo + 
				", rating: " + feedbackEntry.getRating() + 
				", comment: " + formatComment(feedbackEntry.getComment()));
		FeedbackRecord feedbackRecord = FeedbackRecord.fromFeedbackEntryAndQueueInfo(feedbackEntry, queueInfo);
		feedbackService.storeFeedack(feedbackRecord);
		LOGGER.info("postFeedback.");
	}
	
	private QueueInfo getQueueInfoFromFeedback(FeedbackEntry entry) {
		QueueInfo queueInfo = new QueueInfo();
		Integer queueId = entry.getQueueId();
		if(queueId == null) {
			return queueInfo;
		}
		queueInfo = queueService.getQueueInfoByQueueId(queueId);
		return queueInfo;
	}
	
	private String formatComment(String comment) {
		if(StringUtils.isEmpty(comment)) {
			return "NULL";
		}
		return comment;
	}
	
	@RequestMapping(value = "feedbacks", method = RequestMethod.GET)
	@ResponseBody
	public Collection<FeedbackEntry> getFeedbacks(
			@RequestParam(value = "offset", required = false) Integer offset, 
			@RequestParam(value = "limit", required = false) Integer limit, 
			@RequestParam(value = "sort", required = false) String sort, 
			@RequestParam(value = "order", required = false) String order, 
			@RequestParam(value = "queueId", required = false) Integer queueId) {
		offset = InputResolver.resolveOffset(offset);
		limit = InputResolver.resolveLimit(limit);
		sort = InputResolver.resoleveSort(sort);
		order = InputResolver.resoleveOrder(order);
		FeedbackRecordFilter filter = new FeedbackRecordFilterBuilder()
				.withOffset(offset).withLimit(limit)
				.withOrder(order).withSort(sort).forQueue(queueId)
				.build();
		List<FeedbackRecord> feedbackRecords = feedbackService.findFeedbacks(filter);
		List<FeedbackEntry> feedbackEntries = new ArrayList<FeedbackEntry>();
		for(FeedbackRecord record: feedbackRecords) {
			feedbackEntries.add(FeedbackEntry.fromFeedbackRecord(record));
		}
		return feedbackEntries;
	}
	
	private static final class InputResolver {
		
		private static final Set<String> VALID_SORT = new HashSet<String>();
		private static final Set<String> VALID_ORDER = new HashSet<String>();
		
		static {
			VALID_SORT.add("rating");
			VALID_SORT.add("created");
			VALID_SORT.add("queueId");
			VALID_ORDER.add("asc");
			VALID_ORDER.add("desc");
		}
	
		public static String resoleveSort(String inputSort) {
			String defaultSort = "created";
			if(StringUtils.isEmpty(inputSort)) {
				return defaultSort;
			}
			if(!VALID_SORT.contains(inputSort)) {
				return defaultSort;
			}
			if(inputSort.equals("queueId")) {
				return "queue_id";
			}
			return inputSort;
		}
		
		public static String resoleveOrder(String inputOrder) {
			String defaultOrder = "desc";
			if(StringUtils.isEmpty(inputOrder)) {
				return defaultOrder;
			}
			if(!VALID_ORDER.contains(inputOrder)) {
				return defaultOrder;
			}
			return inputOrder;
		}
		
		public static int resolveLimit(Integer limit) {
			int defaultLimit = 10;
			int minLimit = 1;
			int maxLimit = 20;
			if(limit == null || limit < minLimit || limit > maxLimit) {
				return defaultLimit;
			}
			return limit;
		}
		
		public static int resolveOffset(Integer inputOffset) {
			int defaultOffset = 0;
			int minOffset = 0;
			if(inputOffset == null || inputOffset < minOffset) {
				return defaultOffset;
			}
			return inputOffset;
		}
	}
}
