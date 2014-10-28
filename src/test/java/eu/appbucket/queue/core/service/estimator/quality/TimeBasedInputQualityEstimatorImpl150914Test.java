package eu.appbucket.queue.core.service.estimator.quality;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TimeBasedInputQualityEstimatorImpl150914Test {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private TimeBasedInputQualityEstimatorImpl estimator;
	private long openingTime;
	private long closingTime;
	private DateStore dataStore;
	
	private class UserEntry {
		
		private Date date;
		private int number;
		private int quality;
		
		public void setQuality(int quality) {
			this.quality = quality;
		}
		
		public int getQuality() {
			return quality;
		}
		
		public void setDate(Date date) {
			this.date = date;
		}
		
		public void setNumber(int number) {
			this.number = number;
		}
		
		public Date getDate() {
			return date;
		}
		
		public int getNumber() {
			return number;
		}

		@Override
		public String toString() {
			return "UserEntry ["
					+ "date=" + DATE_FORMATTER.format(date) 
					+ ", number=" + number
					+ ", quality=" + quality + "]";
		}
		
		
	}
	
	private class DateStore {
		
		
		private UserEntry userEntry;
		private List<UserEntry> userEntries = new ArrayList<UserEntry>();
		
		public void addEntry(String date, int serverdTicket) {
			userEntry = new UserEntry();
			userEntry.setDate(getDateFromString(date));
			userEntry.setNumber(serverdTicket);
			userEntries.add(userEntry);
		}
		
		public List<UserEntry> getUserEntries() {
			return userEntries;
		}
	}

	private Date getDateFromString(String dateInString) {
		try {
			return DATE_FORMATTER.parse(dateInString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Before
	public void setup() {
		estimator = new TimeBasedInputQualityEstimatorImpl();
		openingTime = getDateFromString("15/09/2014 08:00:00").getTime();
		closingTime = getDateFromString("15/09/2014 21:00:00").getTime();
		dataStore = new DateStore();
		dataStore.addEntry("15/09/2014 09:05:37", 25);
		dataStore.addEntry("15/09/2014 09:07:41", 26);
		dataStore.addEntry("15/09/2014 09:17:12", 36);
		dataStore.addEntry("15/09/2014 09:27:06", 44);
		dataStore.addEntry("15/09/2014 09:28:17", 45);
		dataStore.addEntry("15/09/2014 09:28:28", 45);
		dataStore.addEntry("15/09/2014 09:28:31", 45);
		dataStore.addEntry("15/09/2014 09:30:06", 45);
		dataStore.addEntry("15/09/2014 09:30:07", 45);
		dataStore.addEntry("15/09/2014 09:30:24", 46);
		dataStore.addEntry("15/09/2014 09:30:58", 46);
		dataStore.addEntry("15/09/2014 09:37:05", 52);
		dataStore.addEntry("15/09/2014 09:48:03", 58);
		dataStore.addEntry("15/09/2014 09:50:27", 60);
		dataStore.addEntry("15/09/2014 09:56:04", 61);
		dataStore.addEntry("15/09/2014 10:35:42", 88);
		dataStore.addEntry("15/09/2014 10:48:36", 92);
		dataStore.addEntry("15/09/2014 10:52:40", 92);
		dataStore.addEntry("15/09/2014 10:53:05", 92);
		dataStore.addEntry("15/09/2014 10:58:19", 93);
		dataStore.addEntry("15/09/2014 11:10:56", 96);
		dataStore.addEntry("15/09/2014 11:13:04", 97);
		dataStore.addEntry("15/09/2014 11:25:57", 102);
		dataStore.addEntry("15/09/2014 11:32:36", 105);
		dataStore.addEntry("15/09/2014 11:33:00", 106);
		dataStore.addEntry("15/09/2014 11:37:10", 112);
		dataStore.addEntry("15/09/2014 11:43:57", 115);
		dataStore.addEntry("15/09/2014 12:21:09", 134);
		dataStore.addEntry("15/09/2014 12:28:43", 144);
		dataStore.addEntry("15/09/2014 12:29:14", 145);
		dataStore.addEntry("15/09/2014 12:32:32", 146);
		dataStore.addEntry("15/09/2014 12:32:49", 146);
		dataStore.addEntry("15/09/2014 12:33:09", 149);
		dataStore.addEntry("15/09/2014 13:20:25", 172);
		dataStore.addEntry("15/09/2014 13:21:35", 172);
		dataStore.addEntry("15/09/2014 13:33:34", 172);
		dataStore.addEntry("15/09/2014 13:33:42", 178);
		dataStore.addEntry("15/09/2014 14:45:36", 218);
		dataStore.addEntry("15/09/2014 15:05:46", 218);
		dataStore.addEntry("15/09/2014 15:06:00", 229);
		dataStore.addEntry("15/09/2014 15:06:14", 229);
		dataStore.addEntry("15/09/2014 15:09:05", 231);
		dataStore.addEntry("15/09/2014 15:17:10", 233);
		dataStore.addEntry("15/09/2014 16:09:37", 254);
		dataStore.addEntry("15/09/2014 16:22:10", 261);
		dataStore.addEntry("15/09/2014 16:22:56", 263);
		dataStore.addEntry("15/09/2014 19:09:59", 361);
		dataStore.addEntry("15/09/2014 19:10:17", 362);
		dataStore.addEntry("15/09/2014 20:24:41", 400);
		dataStore.addEntry("15/09/2014 20:25:38", 400);
	}

	@Test
	public void test() {
		estimateDataStoryEntriesQuality();
		assertQualityPercentageIsAcceptable(80);
	}
	
	private void estimateDataStoryEntriesQuality() {
		for(UserEntry entry: dataStore.getUserEntries()) {
			entry.setQuality(estimateEntryQuality(entry));
			System.out.println(entry);
		}
		
	}
	
	private int estimateEntryQuality(UserEntry entry) {
		// long averageWaitingTime = 144000L;
		long averageWaitingTime = 122390L;
		int quality = estimator.estimateQuality(
				entry.getNumber(), 
				averageWaitingTime, 
				entry.getDate().getTime(), 
				openingTime, 
				closingTime);
		return quality;
	}
	
	public void assertQualityPercentageIsAcceptable(int expectedMinimumPercentage){
		int totalNumberOfEntries = dataStore.getUserEntries().size();
		int totalNumberOfEntriesWithQualityAboveZero = 0;
		for(UserEntry entry: dataStore.getUserEntries()) {
			if(entry.getQuality() > 0) {
				totalNumberOfEntriesWithQualityAboveZero++;
			}
		}
		int percentageOfEntriesWithQualityAboveZero = (100 * totalNumberOfEntriesWithQualityAboveZero) / totalNumberOfEntries;
		if(percentageOfEntriesWithQualityAboveZero < expectedMinimumPercentage) {
			throw new AssertionError("percentageOfEntriesWithQualityAboveZero: " + percentageOfEntriesWithQualityAboveZero);
		}
	}
}