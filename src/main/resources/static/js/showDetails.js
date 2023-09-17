const detailsDiv = document.getElementById('details-ticket');

// show details of tickets selected
function loadTicketDetails(ticketId) {
    var context = document.querySelector('base').getAttribute('href');
    var url = context + "ticket/"+ticketId+"/show";
    var options = {method : "GET"};

    fetch(url, options)
        .then(response => {
            if (response.status === 200)
                return response.json();
        })
        .then(ticket => {
            detailsDiv.innerHTML =
                detailsDiv.innerHTML =
                    '<div class="card flex" style="height: 28vh;">\n' +
                    '<div class="card-header text-center">\n' +
                    'Details ticket\n' +
                    '<a class="btn btn-outline-danger" style="margin-left: 2rem;" href="/board">Close</a> </div>\n' +
                    '<div class="card-body" style="height: 10rem; overflow-y: auto;">\n ' +
                    '<div>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Title: <span class="fw-normal">'+ticket.title+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Status: <span class="fw-normal">'+ticket.status+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Author: <span class="fw-normal">'+ticket.author.username+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Assignee: <span class="fw-normal">'+ticket.assignee+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Type: <span class="fw-normal">'+ticket.type+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Date: <span class="fw-normal">'+ticket.date+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Due date: <span class="fw-normal">'+ticket.due_date+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Estimate: <span class="fw-normal">'+ticket.estimate+'</span></p>\n ' +
                    '<p class="mt-2 fw-bold mb-1">Time_spent: <span class="fw-normal">'+ticket.time_spent+'</span></p>\n ' +
                    '</div>\n ' +
                    '</div>\n ' +
                    '</div>';
        })
        .catch(error => {
            console.log(error);
        });
}

