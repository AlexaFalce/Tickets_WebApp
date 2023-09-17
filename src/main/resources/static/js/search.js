const searchInput = document.querySelector("#search-input");

// listener on the search bar
searchInput.addEventListener("keyup", function(){
    if(searchInput.value.length>=3)
        search();
})

// search string insert and show result
function search() {
    var context = document.querySelector('base').getAttribute('href');
    var options = {method : "GET"};
    var url = context + "ticket/search?q="+searchInput.value;

    fetch(url, options).then(function(response){
        return response.json();
    }).then(function(tickets){
        const container = document.querySelector("#content_container");

        let articles = '';
        for (let i = 0; i < tickets.length; i++) {
            var maxLength = 55;
            var titleText = tickets[i].title;
            var title = titleText.length > maxLength ? titleText.substring(0, maxLength) + '...' : titleText;

            articles +=
                '<div class="col-md-' + 3 + '">' +
                '<div class="card mb-3 border-primary" style="width: 18rem; height: 14rem;">' +
                '    <div class="card-header" style="height: 4rem;">' +
                '        <b><p class="mb-0">' + tickets[i].author.username + '</p></b>' +
                '        <small class="text-muted">' + tickets[i].date + '</small>' +
                '    </div>' +
                '    <div class="card-body text-center">' +
                '        <h5 class="card-title">' + title + '</h5>' +
                '    </div>' +
                '    <div class="card-footer bg-transparent">' +
                '        <a class="btn btn-outline-primary btn-sm float-end me-2 col-3" href="' + context + 'ticket/' + tickets[i].id + '">Details</a>' +
                '        <a class="btn btn-outline-primary btn-sm float-end me-2 col-3" href="' + context + 'ticket/' + tickets[i].id + '/edit">Edit</a>' +
                '    </div>' +
                '</div>' +
                '</div>';
        }

        if (tickets.length === 0) {
            articles = '<p class="fs-5">No ticket found</p>';
        }

        container.innerHTML =
            '<div class="d-flex align-items-center pt-3 pb-2 mb-3 border-bottom"">\n' +
            '    <a class="btn btn-sm btn-outline-danger" style="margin-right: 5px;" href="'+window.location.href+'">Close</a> \n' +
            '    <h2 class="mb-0">Risultati ricerca: '+searchInput.value+'</h2> \n' +
            '</div>' +

            '<div class="row">\n' +
                articles +
            '</div>\n';
    });

}
