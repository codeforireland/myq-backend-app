package eu.appbucket.queue.core.service.estimator.duration;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import eu.appbucket.queue.core.domain.queue.OpeningTimes;
import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueStats;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;

public class CalculatedWaitingTimeEsimationStrategyImplTest {
	
	CalculatedWaitingTimeEsimationStrategyImpl sut = null;
	
	@Before
	public void setup() {
		sut = new CalculatedWaitingTimeEsimationStrategyImpl();
	}
	
	@Test
	public void test(){
		long openingTimeTimestamp = 25;
		int averageWaitingTime = 50;
		int ticketNumber = 100;
		final OpeningTimes todayOpeningTimesUTC = new OpeningTimes();
		todayOpeningTimesUTC.setOpeningTime(openingTimeTimestamp);
		QueueDetails queueDetails = new QueueDetails() {
			public OpeningTimes getTodayOpeningTimesUTC() {
				return todayOpeningTimesUTC;
			}
		};
		QueueStats queueStats = new QueueStats();
		queueStats.setCalculatedAverageWaitingDuration(averageWaitingTime);
		TicketEstimation ticketStatus = sut.estimateTimeToBeServiced(queueDetails, queueStats, ticketNumber);
		long actualServiceTime = ticketStatus.getTimeToBeServiced();
		long expectedServiceTime = (averageWaitingTime * (ticketNumber -1)) + openingTimeTimestamp;
		assertEquals(expectedServiceTime, actualServiceTime);
	}
	
	@Test
	public void testBug2014Oct14WaitTimeNotCorrect() throws ParseException {
		Date openingTime = getDateFromString("14-10-2014 07:00:00");
		int averageWaitingTime = 712295;
		int ticketNumber = 365;
		final OpeningTimes todayOpeningTimesUTC = new OpeningTimes();
		todayOpeningTimesUTC.setOpeningTime(openingTime.getTime());
		QueueDetails queueDetails = new QueueDetails() {
			public OpeningTimes getTodayOpeningTimesUTC() {
				return todayOpeningTimesUTC;
			}
		};
		QueueStats queueStats = new QueueStats();
		queueStats.setCalculatedAverageWaitingDuration(averageWaitingTime);
		TicketEstimation ticketStatus = sut.estimateTimeToBeServiced(queueDetails, queueStats, ticketNumber);
		System.out.println("timeToBeServiced - time stamp: " + ticketStatus.getTimeToBeServiced());
		System.out.println("timeToBeServiced - date: " + new Date(ticketStatus.getTimeToBeServiced()));
		/*Date nowTimeStamp = getDateFromString("14-10-2014 19:11:23");
		System.out.println("nowTimeStamp: " + nowTimeStamp.getTime());
		Date dateSome = new Date(215392380 * 1000);
		System.out.println("some date: " + dateSome);*/
	}
	
	private Date getDateFromString(String dateInString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		Date date = sdf.parse(dateInString);
		return date;
	}
}
