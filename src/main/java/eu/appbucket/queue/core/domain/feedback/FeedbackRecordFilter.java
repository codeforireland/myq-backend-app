package eu.appbucket.queue.core.domain.feedback;

public class FeedbackRecordFilter {
	
	private int offset;
	private int limit;
	private String sort;
	private String order;
	private Integer queueId;
	
	private FeedbackRecordFilter() {
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public static class FeedbackRecordFilterBuilder {
		
		private int offset;
		private int limit;
		private String sort;
		private String order;
		private Integer queueId;
		
		public FeedbackRecordFilterBuilder withOffset(int offset) {
			this.offset = offset;
			return this;
		}
		
		public FeedbackRecordFilterBuilder withLimit(int limit) {
			this.limit = limit; 
			return this;
		}
		
		public FeedbackRecordFilterBuilder withSort(String sort) {
			this.sort = sort; 
			return this;
		}
		
		public FeedbackRecordFilterBuilder withOrder(String order) {
			this.order = order; 
			return this;
		}
		
		public FeedbackRecordFilterBuilder forQueue(Integer queueId) {
			this.queueId = queueId; 
			return this;
		}
		
		public FeedbackRecordFilter build() {
			FeedbackRecordFilter filter = new FeedbackRecordFilter();
			filter.setLimit(this.limit);
			filter.setOffset(this.offset);
			filter.setOrder(this.order);
			filter.setSort(this.sort);
			filter.setQueueId(this.queueId);
			return filter;
		}
	};
}
