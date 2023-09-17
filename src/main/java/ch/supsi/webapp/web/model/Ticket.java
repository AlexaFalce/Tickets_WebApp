package ch.supsi.webapp.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity @AllArgsConstructor
public class Ticket implements DateConverter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Type type;
    @ManyToOne
    private User author;
    private Status status;
    private String date;
    @Embedded @JsonIgnore
    private Attachment attachment;
    private String due_date;
    private String assignee;
    private double estimate;
    private double time_spent;
    @ManyToMany @JsonIgnore
    private final Set<User> watchers = new HashSet<>();


    public Ticket() {
        setDate(convertDateFromLocalDateTimeToString(LocalDateTime.now(), getPersonalisedDateFormat()));
        setStatus(Status.OPEN);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public double getEstimate() {
        return estimate;
    }

    public void setEstimate(double estimate) {
        this.estimate = estimate;
    }

    public double getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(double time_spent) {
        this.time_spent = time_spent;
    }

    public String getOriginalDateFormat() {
        return "yyyy-MM-dd'T'hh:mm";
    }

    public String getPersonalisedDateFormat() {
        return "dd MMM yyyy HH:mm";
    }

    public Set<User> getWatchers() {
        return watchers;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", author=" + author +
                ", status=" + status +
                ", date='" + date + '\'' +
                ", attachment=" + attachment +
                ", due_date='" + due_date + '\'' +
                ", assignee='" + assignee + '\'' +
                ", estimate=" + estimate +
                ", time_spent=" + time_spent +
                ", watchers=" + watchers +
                '}';
    }

    public boolean isValid() {
        return !title.equals("") &&
                !description.equals("") &&
                !date.equals("") &&
                type != null &&
                status != null &&
                !due_date.equals("") &&
                !assignee.equals("") &&
                estimate >= 0.0;
    }

}
