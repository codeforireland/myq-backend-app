package eu.appbucket.queue.core.service.estimator.quality;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TimeBasedInputQualityEstimatorImplForRealDataTest {

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
		
	}

	@Test
	public void test_150914() {
		prepareDate_150914();
		estimateDataStoryEntriesQuality();
		assertQualityPercentageIsAcceptable(40);
	}
	
	private void prepareDate_150914() {
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
		/*dataStore.addEntry("15/09/2014 20:24:41", 400);
		dataStore.addEntry("15/09/2014 20:25:38", 400);*/
	}
	private void estimateDataStoryEntriesQuality() {
		int numberOfEntierWithQualityAbove0 = 0;
		for(UserEntry entry: dataStore.getUserEntries()) {
			entry.setQuality(estimateEntryQuality(entry));
			if(entry.getQuality() > 0) {
				numberOfEntierWithQualityAbove0++;
			}
			if(numberOfEntierWithQualityAbove0 > 5 && entry.getQuality() > 0) {
				recalculateDefaultAverageWaitingTime();
			}
			System.out.println(entry);
		}
		
	}
	
	private void recalculateDefaultAverageWaitingTime() {
		
	}
	
	private int estimateEntryQuality(UserEntry entry) {
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
			throw new AssertionError(
					"number of entries with quality above 0: " + totalNumberOfEntriesWithQualityAboveZero  + 
					" out of total number of entries: " + totalNumberOfEntries + 
					" percentageOfEntriesWithQualityAboveZero: " + percentageOfEntriesWithQualityAboveZero);
		}
	}
	
	@Test
	public void test_071014() {
		prepareDate_071014();
		estimateDataStoryEntriesQuality();
		assertQualityPercentageIsAcceptable(40);
	}
	
	private void prepareDate_071014() {
		openingTime = getDateFromString("07/10/2014 08:00:00").getTime();
		closingTime = getDateFromString("07/10/2014 21:00:00").getTime();
		dataStore = new DateStore();
		dataStore.addEntry("07/10/2014 08:51:26", 21);
		dataStore.addEntry("07/10/2014 08:51:44", 27);
		dataStore.addEntry("07/10/2014 08:52:47", 27);
		dataStore.addEntry("07/10/2014 08:57:47", 38);
		dataStore.addEntry("07/10/2014 09:27:40", 59);
		/*dataStore.addEntry("07/10/2014 09:28:25", 59);
		dataStore.addEntry("07/10/2014 09:28:39", 59);
		dataStore.addEntry("07/10/2014 09:29:04", 59);
		dataStore.addEntry("07/10/2014 09:30:20", 59);*/
		dataStore.addEntry("07/10/2014 09:31:28", 62);
		dataStore.addEntry("07/10/2014 11:19:03", 105);
		dataStore.addEntry("07/10/2014 11:35:41", 120);
		dataStore.addEntry("07/10/2014 11:54:50", 124);
		dataStore.addEntry("07/10/2014 11:56:35", 125);
		dataStore.addEntry("07/10/2014 14:05:07", 192);
		dataStore.addEntry("07/10/2014 14:05:52", 192);
		dataStore.addEntry("07/10/2014 15:04:53", 226);
		dataStore.addEntry("07/10/2014 15:39:34", 250);
		dataStore.addEntry("07/10/2014 16:39:41", 264);
		dataStore.addEntry("07/10/2014 16:40:23", 265);
		dataStore.addEntry("07/10/2014 18:20:51", 323);
		dataStore.addEntry("07/10/2014 18:32:56", 331);
		/*dataStore.addEntry("07/10/2014 19:07:14", 331);*/
		dataStore.addEntry("07/10/2014 19:07:27", 344);
		dataStore.addEntry("07/10/2014 19:11:11", 346);
		dataStore.addEntry("07/10/2014 19:14:03", 347);
		dataStore.addEntry("07/10/2014 19:40:20", 351);
		dataStore.addEntry("07/10/2014 19:40:53", 352);
		dataStore.addEntry("07/10/2014 19:44:26", 358);
		dataStore.addEntry("07/10/2014 19:48:01", 359);
		dataStore.addEntry("07/10/2014 19:48:47", 363);
	}
	
	@Test
	public void test_141014() {
		prepareDate_141014();
		estimateDataStoryEntriesQuality();
		assertQualityPercentageIsAcceptable(40);
	}
	
	private void prepareDate_141014() {
		openingTime = getDateFromString("14/10/2014 08:00:00").getTime();
		closingTime = getDateFromString("14/10/2014 21:00:00").getTime();
		dataStore = new DateStore();
		dataStore.addEntry("14/10/2014 08:18:44", 9);
		dataStore.addEntry("14/10/2014 08:19:30", 10);
		dataStore.addEntry("14/10/2014 13:01:45", 152);
		dataStore.addEntry("14/10/2014 13:18:01", 153);
		dataStore.addEntry("14/10/2014 13:29:37", 156);
		dataStore.addEntry("14/10/2014 15:17:50", 155);
		dataStore.addEntry("14/10/2014 19:07:15", 356);
		dataStore.addEntry("14/10/2014 19:10:29", 356);
		dataStore.addEntry("14/10/2014 19:11:23", 358);
		dataStore.addEntry("14/10/2014 19:14:07", 359);
		dataStore.addEntry("14/10/2014 19:16:08", 360);
	}
}