package ch.supsi.webapp.web.service;

import ch.supsi.webapp.web.model.Attachment;
import ch.supsi.webapp.web.model.Status;
import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    private final LinkedHashMap<String, Integer> statusDetails = new LinkedHashMap<>();

    public boolean post(Ticket ticket, MultipartFile attachment, User user) throws IOException {
        if (ticket.isValid()) {
            ticket.setAuthor(user);
            ticket.setDue_date(ticket.convertDateToPersonalisedFormat(ticket.getDue_date(), ticket.getOriginalDateFormat(), ticket.getPersonalisedDateFormat()));
            addAttachment(ticket, attachment);
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    public Ticket getById(int id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.orElse(null);
    }

    public boolean put(Ticket ticket, MultipartFile attachment) throws IOException {
        Ticket originalTicket = ticketRepository.findById(ticket.getId()).get();

        if (ticket.isValid()) {
            ticket.setAuthor(originalTicket.getAuthor());
            ticket.setDue_date(ticket.convertDateToPersonalisedFormat(ticket.getDue_date(), ticket.getOriginalDateFormat(), ticket.getPersonalisedDateFormat()));
            if (attachment.getBytes().length == 0)
                ticket.setAttachment(originalTicket.getAttachment());
            else
                addAttachment(ticket, attachment);
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    private void addAttachment(Ticket ticket, MultipartFile attachment) throws IOException {
        if (!attachment.isEmpty()) {
            ticket.setAttachment(Attachment.builder()
                    .bytes(attachment.getBytes())
                    .name(attachment.getOriginalFilename())
                    .contentType(attachment.getContentType())
                    .size(attachment.getSize())
                    .build()
            );
        }
    }

    public boolean ifExists(int id) {
        return ticketRepository.existsById(id);
    }

    public List<Ticket> list(String search) {
        return ticketRepository.findTop5ByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrAuthorUsernameContainingIgnoreCase(search, search, search);
    }

    public void updateSummaryDetails() {
        statusDetails.clear();
        for(Status status : Status.values())
            statusDetails.put(status.name(), 0);

        for (Ticket ticket : ticketRepository.findAll()) {
            String status = ticket.getStatus().name();
            statusDetails.put(status, statusDetails.get(status)+1);
        }
    }

    public Map<String, Integer> getStatusDetails() {
        return statusDetails;
    }

    public void setTimeSpent(Ticket ticket) {
        Ticket originalTicket = ticketRepository.findById(ticket.getId()).get();
        originalTicket.setTime_spent(ticket.getTime_spent());
        ticketRepository.save(originalTicket);
    }

    public boolean deleteTicket(int id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket!= null && ticket.getWatchers().size()>0) {
            return false;
        }
        ticketRepository.deleteById(id);
        return true;
    }

    public boolean addToWatched(Ticket ticket, User user) {
        if (user == null || ticket == null) return false;
        ticket.getWatchers().add(user);
        ticketRepository.save(ticket);
        return true;
    }

    public boolean removeToWatched(Ticket ticket, User user) {
        if (user == null || ticket == null) return false;
        ticket.getWatchers().remove(user);
        ticketRepository.save(ticket);
        return true;
    }

}
