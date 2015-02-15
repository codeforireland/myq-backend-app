package eu.appbucket.queue.web.controller;

import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.appbucket.queue.core.domain.queue.QueueDetails;
import eu.appbucket.queue.core.domain.queue.QueueInfo;
import eu.appbucket.queue.core.domain.queue.QueueStats;
import eu.appbucket.queue.core.domain.ticket.TicketUpdate;
import eu.appbucket.queue.core.service.QueueService;
import eu.appbucket.queue.core.service.TicketService;
import eu.appbucket.queue.web.domain.office.OfficeDetails;
import eu.appbucket.queue.web.domain.office.OfficeStats;
import eu.appbucket.queue.web.domain.queue.QueueId;

@Controller
public class QueueController {
	
	private static final Logger LOGGER = Logger.getLogger(QueueController.class);
	private QueueService queueService;
	private TicketService ticketService;
	
	@Autowired
	public void setQueueService(QueueService queueService) {
		this.queueService = queueService;
	}
	
	@Autowired
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@RequestMapping(value = "queues", method = RequestMethod.GET)
	@ResponseBody
	public Collection<QueueId> getListOfQueueIds(
			@RequestParam(value = "test", required = false) Integer testFlag) {
		LOGGER.info("getListOfQueueIds, test = " + testFlag);
		Collection<QueueId> queueIds = new HashSet<QueueId>();
		Collection<QueueInfo> queues;
		if(findProductionQueues(testFlag)) {
			queues = queueService.getProductionQueues();
		} else {
			queues = queueService.getTestQueues();
		}
		for(QueueInfo queueInfo: queues) {
			queueIds.add(QueueId.fromQueueInfo(queueInfo));
		}		
		LOGGER.info("getListOfQueueIds - queueIds: " + queueIds);
		return queueIds;
	}
	
	private boolean findProductionQueues(Integer test) {
		if(test == null || test == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Deprecated
	@RequestMapping(value = "queues/{queueId}", method = RequestMethod.GET)
	@ResponseBody
	public OfficeDetails getOfficeDetailsDeprecated(@PathVariable int queueId) {		
		return getOfficeDetails(queueId);
	}
	
	@RequestMapping(value = "queues/{queueId}/details", method = RequestMethod.GET)
	@ResponseBody
	public OfficeDetails getOfficeDetails(@PathVariable int queueId) {
		LOGGER.info("getOfficeDetails - queueId: " + queueId);
		QueueDetails queueDetails =  queueService.getQueueDetailsByQueueId(queueId);
		QueueInfo queueInfo = queueService.getQueueInfoByQueueId(queueId);
		OfficeDetails officeDetails = OfficeDetails.fromQueueData(queueInfo, queueDetails);		
		LOGGER.info("getOfficeDetails - officeDetails: " + officeDetails);
		return officeDetails;
	}
	
	@RequestMapping(value = "queues/details", method = RequestMethod.GET)
	@ResponseBody
	public Collection<OfficeDetails> getListOfOfficeDetails(
			@RequestParam(value = "test", required = false) Integer testFlag) {
		LOGGER.info("getListOfOfficeDetails");
		Collection<OfficeDetails> officeDetails = new HashSet<OfficeDetails>();
		Collection<QueueId> queueIds = getListOfQueueIds(testFlag);
		for(QueueId queueId: queueIds) {
			officeDetails.add(getOfficeDetails(queueId.getQueueId()));
		}
		LOGGER.info("getListOfOfficeDetails - officeDetails: " + officeDetails);
		return officeDetails;
	}
	
	@RequestMapping(value = "queues/{queueId}/stats", method = RequestMethod.GET)
	@ResponseBody
	public OfficeStats getQueueStats(@PathVariable int queueId) {
		LOGGER.info("getQueueStats - queueId: " + queueId);
		QueueDetails queueDetails =  queueService.getQueueDetailsByQueueId(queueId);
		QueueStats queueStats = queueService.getQueueStatsByQueueId(queueId);
		QueueInfo queueInfo = queueService.getQueueInfoByQueueId(queueId);
		TicketUpdate ticketUpdate = ticketService.getHighestTicketUpdatesFromToday(queueInfo);
		OfficeStats officeStats = OfficeStats.fromQueueAndTicketData(queueDetails, queueStats, 
				ticketUpdate );
		LOGGER.info("getQueueStats - officeStats: " + officeStats);
		return officeStats;
	}
}
