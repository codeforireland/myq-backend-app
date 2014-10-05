package eu.appbucket.queue.core.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import eu.appbucket.queue.core.domain.feedback.FeedbackRecord;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecord.Rating;
import eu.appbucket.queue.core.domain.feedback.FeedbackRecordFilter;
import eu.appbucket.queue.core.domain.queue.QueueInfo;

@Repository
public class FeedbackDaoImpl implements FeedbackDao {

	private final static String SQL_INSERT_FEEDBACK_RECORD = 
			"INSERT INTO feedbacks(`rating`, `created`, `comment`, `queue_id`) "
			+ "VALUES (?, ?, ?, ?)";
	
	private JdbcTemplate jdbcTempalte;
	
	@Autowired
	public void setJdbcTempalte(JdbcTemplate jdbcTempalte) {
		this.jdbcTempalte = jdbcTempalte;
	}
	
	public void persistFeedbackRecord(FeedbackRecord record) {
		jdbcTempalte.update(SQL_INSERT_FEEDBACK_RECORD, 
				record.getRating().getRatingValue(),
				record.getCreated(),
				record.getComment(),
				record.getQueueInfo() != null ? record.getQueueInfo().getQueueId() : null);
	}

	public List<FeedbackRecord> findFeedbackRecords(FeedbackRecordFilter filter) {
		FeedbackSelectQueryBuilder builder = new FeedbackSelectQueryBuilder()
			.forQueueId(filter.getQueueId())
			.orderBy(filter.getOrder())
			.sortBy(filter.getSort())
			.startingFrom(filter.getOffset())
			.withTotal(filter.getLimit());
		List<FeedbackRecord> records = jdbcTempalte.query(builder.buildQuery(), new FeedbackRecordMapper());
		return records;
	}
	
	private static final class FeedbackSelectQueryBuilder {
		
		private int offset;
		private int limit;
		private Integer queueId;
		private String order;
		private String sort;
		
		private final static String SQL_SELECT_FEEDBACK_RECORDS_WITH_QUEUE_ID =
				"SELECT * FROM feedbacks "
				+ "WHERE queue_id = %d "
				+ "ORDER BY %s %s "
				+ "LIMIT %d "
				+ "OFFSET %d";
		
		private final static String SQL_SELECT_FEEDBACK_RECORDS_WITHOUT_QUEUE_ID =
				"SELECT * FROM feedbacks "
				+ "ORDER BY %s %s "
				+ "LIMIT %d "
				+ "OFFSET %d";
		
		public FeedbackSelectQueryBuilder startingFrom(int offset) {
			this.offset= offset;
			return this;
		}
		
		public FeedbackSelectQueryBuilder withTotal(int limit) {
			this.limit = limit;
			return this;
		}
		
		public FeedbackSelectQueryBuilder forQueueId(Integer queueId) {
			this.queueId = queueId;
			return this;
		}
		
		public FeedbackSelectQueryBuilder sortBy(String sort) {
			this.sort = sort;
			return this;
		}
		
		public FeedbackSelectQueryBuilder orderBy(String order) {
			this.order = order;
			return this;
		}
		
		private String buildQueryWithQueueId() {
			return String.format(
					SQL_SELECT_FEEDBACK_RECORDS_WITH_QUEUE_ID, 
					this.queueId,
					this.sort, this.order,
					this.limit, this.offset);
		}
		
		private String buildQueryWithoutQueueId() {
			return String.format(
					SQL_SELECT_FEEDBACK_RECORDS_WITHOUT_QUEUE_ID,
					this.sort, this.order,
					this.limit, this.offset);
		}
		
		public String buildQuery() {
			if(this.queueId != null) {
				return this.buildQueryWithQueueId();
			} else {
				return this.buildQueryWithoutQueueId();
			}
		}
	}
	
	private static final class FeedbackRecordMapper implements RowMapper<FeedbackRecord> {
		public FeedbackRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			FeedbackRecord record = new FeedbackRecord();
			QueueInfo queueInfo = new QueueInfo();
			queueInfo.setQueueId(rs.getInt("queue_id"));
			record.setQueueInfo(queueInfo);
			record.setCreated(rs.getTimestamp("created"));
			record.setComment(rs.getString("comment"));
			record.setRating(Rating.getRatingEnumByValue(rs.getInt("rating")));
			return record;
		}		
	}
}
