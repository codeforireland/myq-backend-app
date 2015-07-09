package eu.appbucket.queue.core.service2;

import eu.appbucket.queue.core.domain.queue.OpeningTimes;
import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;
import eu.appbucket.queue.core.persistence.TicketDao;
import eu.appbucket.queue.core.service.QueueService;
import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimationStrategy;
import eu.appbucket.queue.core.service2.estimator.duration.WaitingTimeEstimatorStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service(value="ticketService2")
public class TicketServiceImpl implements TicketService {

    private TicketDao ticketDao;
    private QueueService queueService;
    private WaitingTimeEstimatorStrategyFactory waitingTimeEstimatorStrategyFactory;

    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setWaitingTimeEstimatorStrategyFactory(WaitingTimeEstimatorStrategyFactory waitingTimeEstimatorStrategyFactory) {
        this.waitingTimeEstimatorStrategyFactory = waitingTimeEstimatorStrategyFactory;
    }

    public TicketEstimation getTicketEstimation(QueueInfo queueInfo, QueueDetails queueDetails, int ticketId) {
        queueDetails.getTodayOpeningTimesUTC().getOpeningTime();
        Collection<TicketUpdate> ticketUpdates = getTicketUpdatesFromToday(queueInfo);
        WaitingTimeEstimationStrategy estimatorStrategy = waitingTimeEstimatorStrategyFactory.getStrategy(ticketUpdates.size());
        TicketEstimation ticketEstimation = estimatorStrategy.estimateTimeToBeServiced(queueDetails, ticketUpdates, ticketId);
        return ticketEstimation;
    }

    private Collection<TicketUpdate> getTicketUpdatesFromToday(QueueInfo queueInfo) {
        int queueId = queueInfo.getQueueId();
        Date queueOpeningTime = queueService.getQueueOpeningTimeByQueueId(queueInfo.getQueueId());
        Date queueClosingTime = queueService.getQueueClosingTimeByQueueId(queueId);
        Collection<TicketUpdate> todayTicketUpdates =
                ticketDao.readTicketUpdatesByQueueAndDate(
                        queueInfo,
                        queueOpeningTime, queueClosingTime);
        return todayTicketUpdates;
    }

    public void persistTicketUpdate(TicketUpdate ticketUpdate) {
        ticketDao.storeTicketUpdate(ticketUpdate);
    }


}
