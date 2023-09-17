package ch.supsi.webapp.web.controller;

import ch.supsi.webapp.web.dto.TicketDTO;
import ch.supsi.webapp.web.model.Attachment;
import ch.supsi.webapp.web.model.Ticket;
import org.springframework.security.core.userdetails.User;
import ch.supsi.webapp.web.service.TicketService;
import ch.supsi.webapp.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    private boolean isCreated = true;
    private boolean isEdited = true;
    private boolean isRegistered = true;
    private boolean isDelete = true;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tickets", ticketService.getAll());
        return "homepage";
    }

    @GetMapping("/ticket/{id}")
    public String detailTicket(@PathVariable int id, Model model) {
        if (!ticketService.ifExists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        model.addAttribute("ticket", ticketService.getById(id));
        model.addAttribute("success", isDelete);
        isDelete = true;
        return "detailsTicket";
    }

    @GetMapping("/ticket/new")
    public String crateTicketGet(Model model) {
        Ticket ticket = new Ticket();
        model.addAttribute("ticket",ticket);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("currentDate", ticket.convertDateToPersonalisedFormat(ticket.getDate(), ticket.getPersonalisedDateFormat(), ticket.getOriginalDateFormat()));
        model.addAttribute("success", isCreated);
        isCreated = true;
        return "createTicket";
    }

    @PostMapping("/ticket/new")
    public String crateTicketPost(Ticket ticket, @RequestParam("myfile") MultipartFile file) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        isCreated = ticketService.post(ticket, file, userService.getByUsername(user.getUsername()));
        if (isCreated) return "redirect:/";
        return "redirect:/ticket/new";
    }

    @ResponseBody
    @GetMapping(value = "/ticket/{id}/attachment")
    public ResponseEntity<byte[]> getAttachmentBytes(@PathVariable int id) {
        Attachment attachment = ticketService.getById(id).getAttachment();
        return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getContentType())).body(attachment.getBytes());
    }

    @GetMapping("/ticket/{id}/edit")
    public String editTicketGet(Model model, @PathVariable int id) {
        if (!ticketService.ifExists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        Ticket ticket = ticketService.getById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("currentDate", ticket.convertDateToPersonalisedFormat(ticket.getDate(), ticket.getPersonalisedDateFormat(), ticket.getOriginalDateFormat()));
        ticket.setDue_date(ticket.convertDateToPersonalisedFormat(ticket.getDue_date(), ticket.getPersonalisedDateFormat(), ticket.getOriginalDateFormat()));
        model.addAttribute("success", isEdited);
        isEdited = true;
        return "editTicket";
    }

    @PostMapping("/ticket/{id}/edit")
    public String editTicketPost(Ticket ticket, @RequestParam("myfile") MultipartFile file) throws IOException {
        isEdited = ticketService.put(ticket, file);
        if (isEdited) return "redirect:/";
        return "redirect:/ticket/" + ticket.getId() + "/edit";
    }

    @GetMapping(value = "/ticket/{id}/delete")
    public String delete(@PathVariable int id) {

        if (!ticketService.ifExists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        isDelete = ticketService.deleteTicket(id);
        if (isDelete) return "redirect:/";
        return "redirect:/ticket/" + id;
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerGet(Model model){
        model.addAttribute("newUser", new ch.supsi.webapp.web.model.User());
        model.addAttribute("success", isRegistered);
        isRegistered = true;
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerPost(ch.supsi.webapp.web.model.User user){
        isRegistered = userService.post(user);
        if (isRegistered) return "redirect:/login";
        return "redirect:/register";
    }

    @GetMapping("/ticket/search") @ResponseBody
    public List<TicketDTO> search(@RequestParam("q") String search){
        return ticketService.list(search).stream().map(TicketDTO::ticket2DTO).collect(Collectors.toList());
    }

    @GetMapping("/board")
    public String showBoard(Model model) {
        ticketService.updateSummaryDetails();
        model.addAttribute("tickets", ticketService.getAll());
        model.addAttribute("statusDetails", ticketService.getStatusDetails());
        model.addAttribute("ticketDetails", ticketService.getAll());
        return "boardTicket";
    }

    @GetMapping("/ticket/{id}/time-spent")
    public String timeSpentGet(Model model, @PathVariable int id) {
        if (!ticketService.ifExists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        model.addAttribute("ticket", ticketService.getById(id));
        return "timespent";
    }

    @PostMapping("/ticket/{id}/time-spent")
    public String timeSpentPost(Ticket ticket) {
        ticketService.setTimeSpent(ticket);
        return "redirect:/board";
    }

    @GetMapping("/ticket/{id}/show")
    @ResponseBody
    public Ticket showDetailsTicket(@PathVariable int id) {
        return ticketService.getById(id);
    }

    @GetMapping("/ticket/{id}/add-watched")
    @ResponseBody
    public ResponseEntity<String> addToWatched(@PathVariable int id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ch.supsi.webapp.web.model.User user = userService.getByUsername(principal.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user authenticated");
        boolean checkUser = userService.addToWatched(userService.getByUsername(user.getUsername()), ticketService.getById(id));
        boolean checkTicket = ticketService.addToWatched(ticketService.getById(id), userService.getByUsername(user.getUsername()));
        if (checkUser && checkTicket)
            return ResponseEntity.ok("Added ticket to watched list");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossible to add ticket to watched list: user or ticket not exist");
    }

    @GetMapping("/ticket/{id}/remove-watched")
    @ResponseBody
    public ResponseEntity<String> removeToWatched(@PathVariable int id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ch.supsi.webapp.web.model.User user = userService.getByUsername(principal.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user authenticated");
        boolean checkUser = userService.removeToWatched(userService.getByUsername(user.getUsername()), ticketService.getById(id));
        boolean checkTicket = ticketService.removeToWatched(ticketService.getById(id),userService.getByUsername(user.getUsername()));
        if (checkUser && checkTicket)
            return ResponseEntity.ok("Removed ticket to watched list");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossible to remove ticket to watched list: user or ticket not exist");
    }

    @GetMapping("/get-watched-tickets-count")
    @ResponseBody
    public ResponseEntity<String> getWatchedTicketsCount() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ch.supsi.webapp.web.model.User user = userService.getByUsername(principal.getUsername());
        if (user != null) {
            int watchedCount = userService.getWatched(user.getUsername()).size();
            return ResponseEntity.ok(String.valueOf(watchedCount));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user authenticated");
    }

    @GetMapping("/ticket/{id}/is-watched")
    @ResponseBody
    public ResponseEntity<String> isTicketPresent(@PathVariable int id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ch.supsi.webapp.web.model.User user = userService.getByUsername(principal.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user authenticated");

        Set<Ticket> list = userService.getWatched(user.getUsername());
        if (list == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Watched list not found");
        boolean result = list.contains(ticketService.getById(id));
        return ResponseEntity.ok(String.valueOf(result));
    }

    @GetMapping("/watched-tickets")
    public String getWatchedTickets(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("watchedTickets", userService.getWatched(principal.getUsername()));
        return "watched-tickets";
    }

    @GetMapping("/ticket/{id}/watchers")
    public String getWatchedTickets(Model model, @PathVariable int id) {
        model.addAttribute("watchers", ticketService.getById(id).getWatchers());
        return "watchers";
    }

}

