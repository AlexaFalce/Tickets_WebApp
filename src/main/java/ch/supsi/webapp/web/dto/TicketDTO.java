package ch.supsi.webapp.web.dto;

import ch.supsi.webapp.web.model.*;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data @Builder
public class TicketDTO {
    private int id;
    private String title;
    private String description;
    private Type type;
    private User author;
    private Status status;
    private String date;
    private Attachment attachment;
    private String due_date;
    private String assignee;
    private double estimate;
    private double time_spent;
    private Set<User> watchers;

    public static TicketDTO ticket2DTO(Ticket ticket){
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .date(ticket.getDate())
                .author(ticket.getAuthor())
                .status(ticket.getStatus())
                .type(ticket.getType())
                .attachment(ticket.getAttachment())
                .due_date(ticket.getDue_date())
                .assignee(ticket.getAssignee())
                .estimate(ticket.getEstimate())
                .time_spent(ticket.getTime_spent())
                .watchers(ticket.getWatchers())
                .build();
    }

}
