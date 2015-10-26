package eu.appbucket.queue.web.controller2;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.domain.ticket.TicketEstimation;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;
import eu.appbucket.queue.core.service.QueueService;
import eu.appbucket.queue.core.service2.TicketService;
import eu.appbucket.queue.web.domain.ticket.TicketInput;
import eu.appbucket.queue.web.domain.ticket.TicketStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller(value = "ticketController2")
public class TicketController {

	private static final Logger LOGGER = Logger.getLogger(TicketController.class);
	private QueueService queueService;
	private TicketService ticketService;
	
	@Autowired
	public void setQueueService(QueueService queueService) {
		this.queueService = queueService;
	}
	
	@Autowired
	public void setTicketService2(TicketService ticketService) {
		this.ticketService = ticketService;
	}

    @RequestMapping(value = {"queues/{queueId}/tickets/{ticketId}", "v2/queues/{queueId}/tickets/{ticketId}"}, method = RequestMethod.POST)
    @ResponseBody
    public TicketStatus postTicketUpdate(@PathVariable int queueId, @PathVariable int ticketId,
                                         @RequestBody TicketInput ticketInput) {
        LOGGER.info("postTicketUpdate - queueId: " + queueId + ", ticketId: " + ticketId + ", ticketInput: " + ticketInput);
        saveUserInput(queueId, ticketId, ticketInput);
        TicketStatus ticketStatus = getTicketStatus(queueId, ticketId);
        LOGGER.info("postTicketUpdate - waiting time: " + ticketStatus.getWaitingTime());
        return ticketStatus;
    }

    private void saveUserInput(int queueId, int ticketId, TicketInput ticketInput) {
        QueueInfo queueInfo = queueService.getQueueInfoByQueueId(queueId);
        TicketUpdate ticketUpdate = TicketUpdate.fromTicketInputAndQueueInfo(ticketId, ticketInput, queueInfo);
        ticketService.persistTicketUpdate(ticketUpdate);
    }

    private TicketStatus getTicketStatus(int queueId, int ticketId) {
        QueueInfo queueInfo = queueService.getQueueInfoByQueueId(queueId);
        QueueDetails queueDetails = queueService.getQueueDetailsByQueueId(queueId);
        TicketEstimation ticketEstimation = ticketService.getTicketEstimation(queueInfo, queueDetails, ticketId);
        TicketStatus ticketStatus = TicketStatus.fromTicketEstimation(ticketEstimation);
        LOGGER.info("getTicketStatus - waiting time in milliseconds: " + ticketStatus.getWaitingTime() +
                ", waiting time: " + new Date(ticketStatus.getWaitingTime()));
        return ticketStatus;
    }

	@RequestMapping(value = {"queues/{queueId}/tickets/{ticketId}", "v2/queues/{queueId}/tickets/{ticketId}"}, method = RequestMethod.GET)
	@ResponseBody
	public TicketStatus getTicketStats(@PathVariable int queueId, @PathVariable int ticketId) {
		LOGGER.info("getTicketStats - queueId: " + queueId + ", ticketId: " + ticketId);
        TicketStatus ticketStatus = getTicketStatus(queueId, ticketId);
        LOGGER.info("getTicketStats - waiting time: " + ticketStatus.getWaitingTime());
		return ticketStatus;
	}
	

}
