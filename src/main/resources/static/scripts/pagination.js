// get the table element
var $table = document.getElementById("myTable"),
// number of rows per page
    $n = 24;
var $nextData = [];
var $currentPage = 1;
// if we had one page only, then we have nothing to do ..
/*
if ($pageCount > 1) {
    // assign each row outHTML (tag name & innerHTML) to the array
    for ($i = $j,$ii = 0; $i < $rowCount; $i++, $ii++)
        $tr[$ii] = $table.rows[$i].outerHTML;
    // create a div block to hold the buttons
    $table.insertAdjacentHTML("afterend","<div id='buttons'></div");
    // the first sort, default page is the first one
    sort(1);
}
*/
$table.insertAdjacentHTML("afterend","<div id='buttons'></div>");
sort(1);

function prefetch($p) {
    var http = new XMLHttpRequest();

    http.onreadystatechange = function () {
        if (http.readyState == 4 && http.status == 200 ) {
            $nextData = JSON.parse(http.responseText);
            document.getElementById("buttons").innerHTML = generatePageButtons($p - 1);
        }
    };

    http.open('GET', '/api/anomalousEvents/' + $p, true);
    http.send();
}

// ($p) is the selected page number. it will be generated when a user clicks a button
function sort($p) {
    /* create ($rows) a variable to hold the group of rows
     ** to be displayed on the selected page,
     ** ($s) the start point .. the first row in each page, Do The Math
     */

    if ($p == $currentPage + 1 && $nextData.length > 0) {
        $table.innerHTML = '<thead><tr><th>Time</th><th>Symbol</th><th>Sector</th><th>Currency</th><th>Type</th><th></th></tr></thead>';
        var jsonRows = $nextData;
        // Insert data
        for (var i = 0; i < jsonRows.length; i++) {
            var data = jsonRows[i];
            var row = $table.insertRow();
            var timeCell = row.insertCell(0);
            timeCell.innerHTML = data.time;
            var symbolCell = row.insertCell(1);
            symbolCell.innerHTML = data.symbol;
            var sectorCell = row.insertCell(2);
            sectorCell.innerHTML = data.sector;
            var currencyCell = row.insertCell(3);
            currencyCell.innerHTML = data.currency;
            var typeCell = row.insertCell(4);
            if (data.type == 1) {
                typeCell.innerHTML = 'Individual Trade';
            } else if (data.type == 2) {
                typeCell.innerHTML = 'Aggregate Data';
            } else if (data.type == 3) {
                typeCell.innerHTML = 'Trader Statistics';
            }
            var detailsCell = row.insertCell(5);
            detailsCell.innerHTML = '<a class="button" href="/details/' + data.type + '/' + data.id + '">Details</a>';
        }

        if (jsonRows.length == $n) {
            prefetch($p + 1);
        } else {
            $nextData = [];
            document.getElementById("buttons").innerHTML = generatePageButtons($p);
        }
    } else {
        var http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                // Clear the table
                $table.innerHTML = '<thead><tr><th>Time</th><th>Symbol</th><th>Sector</th><th>Currency</th><th>Type</th><th></th></tr></thead>';


                var jsonRows = JSON.parse(http.responseText);
                // Insert data
                for (var i = 0; i < jsonRows.length; i++) {
                    var data = jsonRows[i];
                    var row = $table.insertRow();
                    var timeCell = row.insertCell(0);
                    timeCell.innerHTML = data.time;
                    var symbolCell = row.insertCell(1);
                    symbolCell.innerHTML = data.symbol;
                    var sectorCell = row.insertCell(2);
                    sectorCell.innerHTML = data.sector;
                    var currencyCell = row.insertCell(3);
                    currencyCell.innerHTML = data.currency;
                    var typeCell = row.insertCell(4);
                    if (data.type == 1) {
                        typeCell.innerHTML = 'Individual Trade';
                    } else if (data.type == 2) {
                        typeCell.innerHTML = 'Aggregate Data';
                    } else if (data.type == 3) {
                        typeCell.innerHTML = 'Trader Statistics';
                    }
                    var detailsCell = row.insertCell(5);
                    detailsCell.innerHTML = '<a class="button" href="/details/' + data.type + '/' + data.id + '">Details</a>';
                }

                if (jsonRows.length == $n) {
                    prefetch($p + 1);
                } else {
                    $nextData = [];
                    document.getElementById("buttons").innerHTML = generatePageButtons($p);
                }
            }
        };

        http.open('GET', '/api/anomalousEvents/' + $p, true);
        http.send();
    }

    $currentPage = $p;
}

function generatePageButtons($cur) {
    /* this variables will disable the "Prev" button on 1st page
     and "next" button on the last one */
    var $prevDis = ($cur == 1)?"disabled":"",
        $nextDis = ($nextData.length == 0)?"disabled":"",
        /* this ($buttons) will hold every single button needed
         ** it will creates each button and sets the onclick attribute
         ** to the "sort" function with a special ($p) number..
         */
        $buttons = "<input type='button' value='&lt;&lt; Prev' onclick='sort("+($cur - 1)+")' "+$prevDis+">";
    $buttons += "<input type='button' value='Next &gt;&gt;' onclick='sort("+($cur + 1)+")' "+$nextDis+">";
    return $buttons;
}
