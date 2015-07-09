package eu.appbucket.queue.core.service2.estimator.duration.regression.record;

import eu.appbucket.queue.core.domain.ticket.TicketUpdate;

import java.util.Date;

public class Record {

    private Date date;
    private int id;
    private int userTicket;
    private int servedTicket;
    private Flag flag = Flag.VALID;
    boolean valid = true;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public int getUserTicket() {
        return userTicket;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        if(flag != Flag.VALID) {
            valid = false;
        }
        this.flag = flag;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserTicket(int userTicket) {
        this.userTicket = userTicket;
    }

    public void setServedTicket(int servedTicket) {
        this.servedTicket = servedTicket;
    }

    public int getServedTicket() {
        return servedTicket;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return convertDateToUnixTimestamp();
    }

    private long convertDateToUnixTimestamp() {
        return this.date.getTime() / 1000;
    }

    public void setTimeStamp(long timeStamp) {
        this.setDate(new Date(timeStamp));
    }

    public static Record fromTicketUpdate(TicketUpdate ticketUpdate) {
        Record record = new Record();
        record.setServedTicket(ticketUpdate.getCurrentlyServicedTicketNumber());
        record.setUserTicket(ticketUpdate.getClientTicketNumber());
        record.setFlag(Flag.VALID);
        record.setValid(true);
        record.setDate(ticketUpdate.getCreated());
        return record;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + date +
                ", servedTicket=" + servedTicket +
                ", flag=" +flag +
                '}';
    }
}
